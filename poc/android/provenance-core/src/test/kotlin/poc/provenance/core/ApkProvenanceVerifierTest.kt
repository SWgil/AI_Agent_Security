package poc.provenance.core

import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Files
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import java.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ApkProvenanceVerifierTest {
    private val issuer = newKeyPair()

    @Test
    fun `attested apk yields its source repository`() {
        val apk = attest(fakeApk())
        val result = ApkProvenanceVerifier.verify(apk, listOf(issuer.public))
        val provenance = assertIs<VerifyResult.Verified>(result).provenance
        assertEquals("alice/notes-app", provenance.repository)
        assertEquals("1001", provenance.repositoryId)
        assertEquals("2002", provenance.ownerId)
        assertEquals("a".repeat(40), provenance.commit)
        assertEquals(listOf("dl.google.com:443", "repo.maven.apache.org:443"), provenance.buildHosts)
        assertEquals(312, provenance.inputArtifactCount)
        ZipFile(apk).use { assertEquals("hello", it.getInputStream(it.getEntry("classes.dex")).reader().readText()) }
    }

    @Test
    fun `apk without attestation is reported as such`() {
        val result = ApkProvenanceVerifier.verify(write(fakeApk()), listOf(issuer.public))
        assertEquals(VerifyResult.NoAttestation, result)
    }

    @Test
    fun `file that is not an apk has no attestation`() {
        val result = ApkProvenanceVerifier.verify(write("not a zip".toByteArray()), listOf(issuer.public))
        assertEquals(VerifyResult.NoAttestation, result)
    }

    @Test
    fun `modified content is rejected`() {
        val bytes = attest(fakeApk()).readBytes()
        val at = String(bytes, Charsets.ISO_8859_1).indexOf("hello")
        bytes[at] = (bytes[at].toInt() xor 1).toByte()
        val result = ApkProvenanceVerifier.verify(write(bytes), listOf(issuer.public))
        assertEquals(VerifyResult.Invalid(InvalidReason.CONTENT_MISMATCH), result)
    }

    @Test
    fun `attestation copied onto another apk is rejected`() {
        val envelope = envelopeFor(fakeApk(payload = "hello"))
        val other = withPair(fakeApk(payload = "evil!"), ApkLayout.PROVENANCE_PAIR_ID, envelope)
        val result = ApkProvenanceVerifier.verify(write(other), listOf(issuer.public))
        assertEquals(VerifyResult.Invalid(InvalidReason.CONTENT_MISMATCH), result)
    }

    @Test
    fun `apk re-signed with another key is rejected`() {
        val envelope = envelopeFor(fakeApk(certificate = "original"))
        val resigned = withPair(fakeApk(certificate = "attacker"), ApkLayout.PROVENANCE_PAIR_ID, envelope)
        val result = ApkProvenanceVerifier.verify(write(resigned), listOf(issuer.public))
        assertEquals(VerifyResult.Invalid(InvalidReason.SIGNER_MISMATCH), result)
    }

    @Test
    fun `attestation from an unknown issuer is rejected`() {
        val result = ApkProvenanceVerifier.verify(attest(fakeApk()), listOf(newKeyPair().public))
        assertEquals(VerifyResult.Invalid(InvalidReason.UNTRUSTED_ISSUER), result)
    }

    @Test
    fun `garbage in the attestation slot is rejected`() {
        val apk = withPair(fakeApk(), ApkLayout.PROVENANCE_PAIR_ID, "{not json".toByteArray())
        val result = ApkProvenanceVerifier.verify(write(apk), listOf(issuer.public))
        assertEquals(VerifyResult.Invalid(InvalidReason.MALFORMED), result)
    }

    @Test
    fun `bundled issuer key matches the signer's public key file`() {
        val pem = File("../../signer/keys/mock_samsung_public_key.pem").readLines()
            .filterNot { it.startsWith("-----") }.joinToString("")
        assertEquals(pem, TrustedIssuers.MOCK_SAMSUNG_SPKI_BASE64)
    }

    /** Cross-check against an APK attested by the Python signer (see poc/e2e.sh). */
    @Test
    fun `real apk attested by the python signer verifies`() {
        val path = System.getProperty("attestedApk").orEmpty()
        if (path.isEmpty()) return
        val result = ApkProvenanceVerifier.verify(File(path))
        val provenance = assertIs<VerifyResult.Verified>(result).provenance
        println("verified real APK from ${provenance.repository} @ ${provenance.commit}")

        val bytes = File(path).readBytes()
        bytes[bytes.size / 3] = (bytes[bytes.size / 3].toInt() xor 1).toByte()
        assertEquals(
            VerifyResult.Invalid(InvalidReason.CONTENT_MISMATCH),
            ApkProvenanceVerifier.verify(write(bytes)),
        )
    }

    // --- helpers: a ZIP with a synthetic signing block shaped like a v2 signature ---

    private fun attest(apk: ByteArray): File =
        write(withPair(apk, ApkLayout.PROVENANCE_PAIR_ID, envelopeFor(apk)))

    private fun envelopeFor(apk: ByteArray): ByteArray {
        val file = write(apk)
        val layout = java.io.RandomAccessFile(file, "r").use { raf ->
            val l = ApkLayout.read(raf)
            l.contentDigest(raf) to l.signerCertDigest()
        }
        val statement = JSONObject()
            .put("type", ApkProvenanceVerifier.STATEMENT_TYPE)
            .put("issuer", "test issuer")
            .put("issuedAt", "2026-01-01T00:00:00Z")
            .put("subject", JSONObject()
                .put("contentDigest", "sha256:${layout.first}")
                .put("signerCertDigest", "sha256:${layout.second}"))
            .put("source", JSONObject()
                .put("repository", "alice/notes-app")
                .put("repositoryId", "1001")
                .put("owner", "alice")
                .put("ownerId", "2002")
                .put("visibility", "public")
                .put("commit", "a".repeat(40))
                .put("ref", "refs/heads/main"))
            .put("build", JSONObject()
                .put("workflowRef", "issuer/builder/.github/workflows/attested-build.yml@refs/heads/main")
                .put("runnerEnvironment", "github-hosted")
                .put("runUrl", "https://github.com/alice/notes-app/actions/runs/42"))
            .put("inputs", JSONObject()
                .put("networkHosts", org.json.JSONArray(listOf("dl.google.com:443", "repo.maven.apache.org:443")))
                .put("artifactCount", 312)
                .put("artifactsDigest", "sha256:" + "0".repeat(64)))
        val payload = statement.toString().toByteArray()
        val signature = Signature.getInstance("SHA256withECDSA").run {
            initSign(issuer.private)
            update(payload)
            sign()
        }
        return JSONObject()
            .put("v", 1)
            .put("statement", Base64.getEncoder().encodeToString(payload))
            .put("signature", Base64.getEncoder().encodeToString(signature))
            .toString().toByteArray()
    }

    private fun fakeApk(payload: String = "hello", certificate: String = "fake-certificate"): ByteArray {
        val zip = ByteArrayOutputStream().also { out ->
            ZipOutputStream(out).use {
                it.setMethod(ZipOutputStream.STORED)
                val bytes = payload.toByteArray()
                val entry = ZipEntry("classes.dex").apply {
                    size = bytes.size.toLong()
                    compressedSize = bytes.size.toLong()
                    crc = java.util.zip.CRC32().apply { update(bytes) }.value
                }
                it.putNextEntry(entry)
                it.write(bytes)
            }
        }.toByteArray()
        val signedData = prefixed(ByteArray(0)) + prefixed(prefixed(certificate.toByteArray())) + prefixed(ByteArray(0))
        val signer = prefixed(signedData) + prefixed(ByteArray(0)) + prefixed(ByteArray(0))
        return insertBlock(zip, listOf(ApkLayout.V2_PAIR_ID to prefixed(prefixed(signer))))
    }

    private fun withPair(apk: ByteArray, id: Int, value: ByteArray): ByteArray {
        val buf = ByteBuffer.wrap(apk).order(ByteOrder.LITTLE_ENDIAN)
        val eocd = eocdOffset(apk)
        val cdStart = buf.getInt(eocd + 16)
        val blockSize = buf.getLong(cdStart - 24).toInt()
        val blockStart = cdStart - blockSize - 8
        val pairs = mutableListOf<Pair<Int, ByteArray>>()
        var pos = blockStart + 8
        while (pos < cdStart - 24) {
            val length = buf.getLong(pos).toInt()
            pairs += buf.getInt(pos + 8) to apk.copyOfRange(pos + 12, pos + 8 + length)
            pos += 8 + length
        }
        val stripped = apk.copyOfRange(0, blockStart) + apk.copyOfRange(cdStart, apk.size)
        ByteBuffer.wrap(stripped).order(ByteOrder.LITTLE_ENDIAN)
            .putInt(eocdOffset(stripped) + 16, blockStart)
        return insertBlock(stripped, pairs.filter { it.first != id } + (id to value))
    }

    private fun insertBlock(zip: ByteArray, pairs: List<Pair<Int, ByteArray>>): ByteArray {
        val body = pairs.fold(ByteArray(0)) { acc, (id, value) ->
            acc + ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
                .putLong(value.size + 4L).putInt(id).array() + value
        }
        val size = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(body.size + 24L).array()
        val block = size + body + size + "APK Sig Block 42".toByteArray()
        val eocd = eocdOffset(zip)
        val cdStart = ByteBuffer.wrap(zip).order(ByteOrder.LITTLE_ENDIAN).getInt(eocd + 16)
        val out = zip.copyOfRange(0, cdStart) + block + zip.copyOfRange(cdStart, zip.size)
        ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN).putInt(eocd + block.size + 16, cdStart + block.size)
        return out
    }

    private fun eocdOffset(zip: ByteArray): Int = zip.size - 22

    private fun prefixed(bytes: ByteArray): ByteArray =
        ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(bytes.size).array() + bytes

    private fun write(bytes: ByteArray): File =
        Files.createTempFile("provenance-test", ".apk").toFile().apply {
            deleteOnExit()
            writeBytes(bytes)
        }

    private fun newKeyPair(): KeyPair =
        KeyPairGenerator.getInstance("EC").apply { initialize(ECGenParameterSpec("secp256r1")) }.generateKeyPair()

    @Suppress("unused")
    private fun sha256(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes)
}
