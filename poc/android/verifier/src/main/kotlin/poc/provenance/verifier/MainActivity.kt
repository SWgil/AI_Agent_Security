package poc.provenance.verifier

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import poc.provenance.core.ApkProvenanceVerifier
import poc.provenance.core.GitHubLookup
import poc.provenance.core.InvalidReason
import poc.provenance.core.Provenance
import poc.provenance.core.RepoSignals
import poc.provenance.core.Severity
import poc.provenance.core.Signal
import poc.provenance.core.SignalKind
import poc.provenance.core.VerifyResult
import java.io.File
import java.time.Instant

class MainActivity : Activity() {
    private lateinit var trustStore: TrustStore
    private lateinit var verdict: TextView
    private lateinit var headline: TextView
    private lateinit var details: TextView
    private lateinit var install: Button
    private lateinit var trust: Button
    private lateinit var trustedList: TextView

    private val candidate: File by lazy { File(cacheDir, "candidate.apk") }

    /** Bumped for each picked file so a slow check cannot overwrite a newer one. */
    private var generation = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        trustStore = TrustStore(this)
        verdict = findViewById(R.id.verdict)
        headline = findViewById(R.id.headline)
        details = findViewById(R.id.details)
        install = findViewById(R.id.install)
        trust = findViewById(R.id.trust)
        trustedList = findViewById(R.id.trusted_list)

        findViewById<Button>(R.id.pick).setOnClickListener {
            val pick = Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(APK_MIME, "application/octet-stream"))
            startActivityForResult(pick, REQUEST_PICK)
        }
        showTrustedList()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) data?.data?.let(::check)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_INSTALL_STATUS -> onInstallStatus(intent)
            Intent.ACTION_VIEW -> intent.data?.let(::check)
            Intent.ACTION_SEND -> streamExtra(intent)?.let(::check)
        }
    }

    @Suppress("DEPRECATION")
    private fun streamExtra(intent: Intent): Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)

    // --- verification ---

    private class Report(
        val result: VerifyResult,
        val signals: List<Signal>,
        val packageInfo: PackageInfo?,
        val appLabel: String?,
    )

    private fun check(uri: Uri) {
        val mine = ++generation
        show(Color.DKGRAY, "확인 중…", "", "")
        install.visibility = View.GONE
        trust.visibility = View.GONE
        Thread {
            val report = try {
                contentResolver.openInputStream(uri).use { input ->
                    requireNotNull(input) { "파일을 열 수 없습니다" }
                    candidate.outputStream().use { input.copyTo(it) }
                }
                inspect(candidate)
            } catch (e: Exception) {
                runOnUiThread {
                    if (mine == generation) show(RED, "파일을 읽지 못했습니다", "", e.message ?: "")
                }
                return@Thread
            }
            runOnUiThread { if (mine == generation) render(report) }
        }.start()
    }

    private fun inspect(apk: File): Report {
        val result = ApkProvenanceVerifier.verify(apk)
        val signals = if (result is VerifyResult.Verified) {
            val lookup = GitHubLookup().lookup(result.provenance.repositoryId)
            RepoSignals.assess(result.provenance, lookup, Instant.now())
        } else {
            emptyList()
        }
        @Suppress("DEPRECATION")
        val info = packageManager.getPackageArchiveInfo(
            apk.path, PackageManager.GET_PERMISSIONS or PackageManager.GET_SERVICES
        )
        val label = info?.applicationInfo?.let {
            it.sourceDir = apk.path
            it.publicSourceDir = apk.path
            it.loadLabel(packageManager).toString()
        }
        return Report(result, signals, info, label)
    }

    private fun render(report: Report) {
        val app = buildString {
            if (report.packageInfo != null) {
                append("앱: ${report.appLabel} (${report.packageInfo.packageName})\n")
            }
        }
        when (val result = report.result) {
            VerifyResult.NoAttestation -> show(
                RED, "출처 증명 없음 — 설치 차단",
                "이 APK에는 어디서 빌드됐는지에 대한 증명이 없습니다.",
                app,
            )
            is VerifyResult.Invalid -> show(
                RED, "검증 실패 — 설치 차단",
                when (result.reason) {
                    InvalidReason.CONTENT_MISMATCH -> "출처 증명 이후에 APK 내용이 바뀌었습니다."
                    InvalidReason.SIGNER_MISMATCH -> "출처 증명 이후에 APK가 다른 키로 다시 서명됐습니다."
                    InvalidReason.UNTRUSTED_ISSUER -> "출처 증명이 신뢰하는 발급자의 서명이 아닙니다."
                    InvalidReason.MALFORMED -> "출처 증명을 해석할 수 없습니다."
                },
                app,
            )
            is VerifyResult.Verified -> renderVerified(result.provenance, report, app)
        }
    }

    private fun renderVerified(p: Provenance, report: Report, app: String) {
        val danger = report.signals.any { it.severity == Severity.DANGER }
        // A registered repository that changed hands is treated as unregistered.
        val trusted = trustStore.isTrusted(p.repositoryId) && !danger
        val risky = riskyPermissions(report.packageInfo)

        val text = buildString {
            append(app)
            append("커밋: ${p.commit.take(12)}\n")
            append("빌드 워크플로: ${p.workflowRef}\n")
            append("발급: ${p.issuedAt}\n")
            append("발급자: ${p.issuer}\n")
            p.buildHosts?.let { hosts ->
                append("\n빌드 중 접속한 외부 호스트 (받아온 파일 ${p.inputArtifactCount}개의 해시 기록됨)\n")
                if (hosts.isEmpty()) append("• 없음\n")
                hosts.forEach { append("• ${it.removeSuffix(":443")}\n") }
            }
            if (report.signals.isNotEmpty()) {
                append("\n확인 결과\n")
                report.signals.forEach { append("${marker(it.severity)} ${describe(it)}\n") }
            }
            if (risky.isNotEmpty()) {
                append("\n이 앱이 요청하는 위험 권한\n")
                risky.forEach { append("• $it\n") }
                if (!trusted) {
                    append("OS에 통합된 구현이라면 미등록 저장소의 앱은 이 권한이 잠긴 채 설치됩니다. ")
                    append("이 PoC는 일반 앱이라 권한을 잠글 수 없어 표시만 합니다.\n")
                }
            }
        }
        show(
            when {
                danger -> RED
                trusted -> GREEN
                else -> AMBER
            },
            when {
                danger -> "출처 확인됨 — 주의 필요"
                trusted -> "신뢰 등록된 저장소"
                else -> "출처 확인됨 (미등록 저장소)"
            },
            "이 APK는 ${p.repository} 저장소에서 제작된 APK입니다.",
            text,
        )

        install.visibility = View.VISIBLE
        install.setOnClickListener {
            if (trusted) {
                startInstall()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("미등록 저장소의 앱")
                    .setMessage("이 APK는 ${p.repository} 저장소에서 제작된 APK입니다. 정말 설치하시겠습니까?")
                    .setPositiveButton("설치") { _, _ -> startInstall() }
                    .setNegativeButton("취소", null)
                    .show()
            }
        }

        trust.visibility = View.VISIBLE
        val registered = trustStore.isTrusted(p.repositoryId)
        trust.text = if (registered) "신뢰 등록 해제" else "이 저장소를 신뢰 목록에 등록"
        trust.setOnClickListener {
            if (registered) trustStore.untrust(p.repositoryId) else trustStore.trust(p.repositoryId, p.repository)
            showTrustedList()
            renderVerified(p, report, app)
        }
    }

    private fun describe(signal: Signal): String = when (signal.kind) {
        SignalKind.REPO_GONE -> "저장소가 삭제됐거나 비공개로 바뀌어 소스를 확인할 수 없습니다."
        SignalKind.LOOKUP_FAILED -> "저장소 상태를 확인하지 못했습니다 (네트워크 또는 요청 한도)."
        SignalKind.OWNER_CHANGED -> "빌드 이후 저장소 소유 계정이 바뀌었습니다."
        SignalKind.REPO_RENAMED -> "저장소 이름이 ${signal.value}(으)로 바뀌었습니다."
        SignalKind.NOT_PUBLIC -> "빌드 당시 소스가 공개 상태가 아니었습니다."
        SignalKind.NEW_REPO -> "만들어진 지 ${signal.value}일 된 저장소입니다."
        SignalKind.NEW_ACCOUNT -> "만들어진 지 ${signal.value}일 된 계정입니다."
        SignalKind.FORK -> "${signal.value ?: "다른 저장소"}의 포크입니다."
        SignalKind.BRAND_KEYWORD -> "저장소 이름에 사칭에 자주 쓰이는 단어(${signal.value})가 있습니다."
        SignalKind.BUILD_INPUTS_NOT_RECORDED -> "빌드가 외부에서 무엇을 받아왔는지 기록되지 않았습니다."
        SignalKind.UNEXPECTED_BUILD_HOST -> "빌드 중 일반적인 패키지 저장소가 아닌 곳에 접속했습니다: ${signal.value}"
        SignalKind.SHARED_HOSTING_BUILD_HOST ->
            "빌드 중 누구나 파일을 올릴 수 있는 곳에 접속했습니다: ${signal.value?.removeSuffix(":443")}. " +
                "패키지 저장소가 일부 파일을 이쪽으로 넘기기도 합니다."
    }

    private fun marker(severity: Severity) = when (severity) {
        Severity.DANGER -> "⛔"
        Severity.WARNING -> "⚠"
        Severity.INFO -> "ℹ"
    }

    private fun riskyPermissions(info: PackageInfo?): List<String> {
        if (info == null) return emptyList()
        val requested = info.requestedPermissions.orEmpty()
            .mapNotNull { RISKY_PERMISSIONS[it] }
        val services = info.services.orEmpty()
            .mapNotNull { RISKY_SERVICE_PERMISSIONS[it.permission] }
        return (requested + services).distinct()
    }

    private fun show(color: Int, verdictText: String, headlineText: String, detailText: String) {
        verdict.setTextColor(color)
        verdict.text = verdictText
        headline.text = headlineText
        details.text = detailText
    }

    private fun showTrustedList() {
        val names = trustStore.names()
        trustedList.text =
            if (names.isEmpty()) "신뢰 등록된 저장소가 없습니다."
            else "신뢰 등록된 저장소\n" + names.joinToString("\n") { "• $it" }
    }

    // --- installation through the system installer ---

    private fun startInstall() {
        if (!packageManager.canRequestPackageInstalls()) {
            startActivity(
                Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName"))
            )
            return
        }
        try {
            val installer = packageManager.packageInstaller
            val sessionId = installer.createSession(
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            )
            installer.openSession(sessionId).use { session ->
                session.openWrite("base.apk", 0, candidate.length()).use { out ->
                    candidate.inputStream().use { it.copyTo(out) }
                    session.fsync(out)
                }
                val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
                val status = Intent(this, MainActivity::class.java).setAction(ACTION_INSTALL_STATUS)
                session.commit(PendingIntent.getActivity(this, sessionId, status, flags).intentSender)
            }
        } catch (e: Exception) {
            details.append("\n설치를 시작하지 못했습니다: ${e.message}\n")
        }
    }

    private fun onInstallStatus(intent: Intent) {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                @Suppress("DEPRECATION")
                val confirm: Intent? = intent.getParcelableExtra(Intent.EXTRA_INTENT)
                confirm?.let(::startActivity)
            }
            PackageInstaller.STATUS_SUCCESS -> details.append("\n설치가 끝났습니다.\n")
            else -> details.append(
                "\n설치되지 않았습니다: ${intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)}\n"
            )
        }
    }

    private companion object {
        const val REQUEST_PICK = 1
        const val APK_MIME = "application/vnd.android.package-archive"
        const val ACTION_INSTALL_STATUS = "poc.provenance.verifier.INSTALL_STATUS"

        val RED = Color.rgb(0xB3, 0x26, 0x1E)
        val AMBER = Color.rgb(0x9A, 0x67, 0x00)
        val GREEN = Color.rgb(0x1B, 0x6E, 0x3C)

        val RISKY_PERMISSIONS = mapOf(
            "android.permission.READ_SMS" to "문자 읽기",
            "android.permission.RECEIVE_SMS" to "문자 수신",
            "android.permission.SEND_SMS" to "문자 보내기",
            "android.permission.READ_CALL_LOG" to "통화 기록 읽기",
            "android.permission.READ_CONTACTS" to "연락처 읽기",
            "android.permission.SYSTEM_ALERT_WINDOW" to "다른 앱 위에 표시",
            "android.permission.REQUEST_INSTALL_PACKAGES" to "다른 앱 설치",
        )
        val RISKY_SERVICE_PERMISSIONS = mapOf(
            "android.permission.BIND_ACCESSIBILITY_SERVICE" to "접근성 서비스",
            "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" to "알림 읽기",
        )
    }
}
