package poc.provenance.core

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.Duration
import java.time.Instant

/** Current state of the attested repository, looked up by its immutable id. */
data class RepoInfo(
    val fullName: String,
    val ownerId: String,
    val createdAt: Instant,
    val ownerCreatedAt: Instant?,
    val isFork: Boolean,
    val parentFullName: String?,
)

sealed interface RepoLookup {
    data class Found(val info: RepoInfo) : RepoLookup

    /** Deleted, or no longer public. */
    data object Gone : RepoLookup
    data class Unavailable(val message: String) : RepoLookup
}

data class HttpResult(val status: Int, val body: String)

/**
 * Looks repositories and accounts up by numeric id rather than by name: a name
 * can be released and claimed by someone else, an id cannot.
 */
class GitHubLookup(private val httpGet: (String) -> HttpResult = ::defaultHttpGet) {

    fun lookup(repositoryId: String): RepoLookup {
        if (!repositoryId.all(Char::isDigit) || repositoryId.isEmpty()) return RepoLookup.Gone
        return try {
            val repoResponse = httpGet("$API/repositories/$repositoryId")
            when (repoResponse.status) {
                200 -> {}
                404, 451 -> return RepoLookup.Gone
                else -> return RepoLookup.Unavailable("HTTP ${repoResponse.status}")
            }
            val repo = JSONObject(repoResponse.body)
            val ownerId = repo.getJSONObject("owner").get("id").toString()
            val ownerResponse = httpGet("$API/user/$ownerId")
            val ownerCreatedAt = if (ownerResponse.status == 200) {
                Instant.parse(JSONObject(ownerResponse.body).getString("created_at"))
            } else {
                null
            }
            RepoLookup.Found(
                RepoInfo(
                    fullName = repo.getString("full_name"),
                    ownerId = ownerId,
                    createdAt = Instant.parse(repo.getString("created_at")),
                    ownerCreatedAt = ownerCreatedAt,
                    isFork = repo.optBoolean("fork"),
                    parentFullName = repo.optJSONObject("parent")?.optString("full_name"),
                )
            )
        } catch (e: IOException) {
            RepoLookup.Unavailable(e.message ?: e.javaClass.simpleName)
        } catch (e: JSONException) {
            RepoLookup.Unavailable("unexpected response")
        } catch (e: java.time.format.DateTimeParseException) {
            RepoLookup.Unavailable("unexpected response")
        }
    }

    companion object {
        private const val API = "https://api.github.com"

        fun defaultHttpGet(url: String): HttpResult {
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000
                connection.setRequestProperty("Accept", "application/vnd.github+json")
                connection.setRequestProperty("User-Agent", "provenance-poc")
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                val body = stream?.use { it.readBytes().toString(Charsets.UTF_8) } ?: ""
                return HttpResult(status, body)
            } finally {
                connection.disconnect()
            }
        }
    }
}

enum class Severity { INFO, WARNING, DANGER }

enum class SignalKind {
    /** The repository was deleted or is no longer public. */
    REPO_GONE,

    /** The repository state could not be checked (offline, rate limit). */
    LOOKUP_FAILED,

    /** The repository now belongs to a different account. Value: none. */
    OWNER_CHANGED,

    /** The repository has a different name now. Value: current name. */
    REPO_RENAMED,

    /** The source was not public when the APK was built. */
    NOT_PUBLIC,

    /** Value: age in days. */
    NEW_REPO,

    /** Value: age in days. */
    NEW_ACCOUNT,

    /** Value: parent repository name. */
    FORK,

    /** The name contains a brand often impersonated by phishing. Value: the keyword. */
    BRAND_KEYWORD,

    /** The attestation does not say what the build downloaded. */
    BUILD_INPUTS_NOT_RECORDED,

    /** The build contacted a host that is not a well-known package source. Value: the host. */
    UNEXPECTED_BUILD_HOST,
}

data class Signal(val kind: SignalKind, val severity: Severity, val value: String? = null)

object RepoSignals {
    const val NEW_REPO_DAYS = 30L
    const val NEW_ACCOUNT_DAYS = 90L

    /** Illustrative only; a real list would be maintained and much longer. */
    private val BRAND_KEYWORDS = listOf(
        "bank", "samsung", "kakao", "naver", "toss", "shinhan", "kbstar", "wooribank",
        "nonghyup", "police", "gov", "postoffice", "cjlogistics",
    )

    /**
     * Package registries and toolchain hosts an ordinary Gradle build uses.
     * Illustrative; a real issuer would maintain this as policy.
     */
    val KNOWN_BUILD_HOSTS = setOf(
        "repo.maven.apache.org:443",
        "dl.google.com:443",
        "maven.google.com:443",
        "plugins.gradle.org:443",
        "plugins-artifacts.gradle.org:443",
        "services.gradle.org:443",
        "downloads.gradle.org:443",
    )

    fun assess(provenance: Provenance, lookup: RepoLookup, now: Instant): List<Signal> {
        val signals = mutableListOf<Signal>()
        when (lookup) {
            is RepoLookup.Gone -> signals += Signal(SignalKind.REPO_GONE, Severity.DANGER)
            is RepoLookup.Unavailable -> signals += Signal(SignalKind.LOOKUP_FAILED, Severity.WARNING)
            is RepoLookup.Found -> {
                val info = lookup.info
                if (info.ownerId != provenance.ownerId) {
                    signals += Signal(SignalKind.OWNER_CHANGED, Severity.DANGER)
                }
                if (!info.fullName.equals(provenance.repository, ignoreCase = true)) {
                    signals += Signal(SignalKind.REPO_RENAMED, Severity.WARNING, info.fullName)
                }
                val repoAge = Duration.between(info.createdAt, now).toDays()
                if (repoAge < NEW_REPO_DAYS) {
                    signals += Signal(SignalKind.NEW_REPO, Severity.WARNING, repoAge.toString())
                }
                info.ownerCreatedAt?.let {
                    val accountAge = Duration.between(it, now).toDays()
                    if (accountAge < NEW_ACCOUNT_DAYS) {
                        signals += Signal(SignalKind.NEW_ACCOUNT, Severity.WARNING, accountAge.toString())
                    }
                }
                if (info.isFork) {
                    signals += Signal(SignalKind.FORK, Severity.INFO, info.parentFullName)
                }
            }
        }
        if (provenance.visibility != "public") {
            signals += Signal(SignalKind.NOT_PUBLIC, Severity.WARNING)
        }
        val name = provenance.repository.lowercase()
        BRAND_KEYWORDS.firstOrNull { it in name }?.let {
            signals += Signal(SignalKind.BRAND_KEYWORD, Severity.WARNING, it)
        }
        val buildHosts = provenance.buildHosts
        if (buildHosts == null) {
            signals += Signal(SignalKind.BUILD_INPUTS_NOT_RECORDED, Severity.INFO)
        } else {
            buildHosts.filterNot { it in KNOWN_BUILD_HOSTS }.forEach {
                signals += Signal(SignalKind.UNEXPECTED_BUILD_HOST, Severity.WARNING, it)
            }
        }
        return signals
    }
}
