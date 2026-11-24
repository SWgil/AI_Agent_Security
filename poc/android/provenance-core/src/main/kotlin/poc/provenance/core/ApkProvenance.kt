package poc.provenance.core

import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

/** What a verified attestation says about where an APK came from. */
data class Provenance(
    val issuer: String,
    val issuedAt: String,
    val repository: String,
    val repositoryId: String,
    val owner: String,
    val ownerId: String,
    val visibility: String,
    val commit: String,
    val ref: String,
    val workflowRef: String,
    val runnerEnvironment: String,
    val runUrl: String,
    /** Hosts the build contacted, as "host:port"; null if the build was not recorded. */
    val buildHosts: List<String>?,
    /** Number of downloaded files whose hashes the issuer recorded. */
    val inputArtifactCount: Int,
)

enum class InvalidReason {
    /** The attestation cannot be parsed. */
    MALFORMED,

    /** The attestation is not signed by a trusted issuer key. */
    UNTRUSTED_ISSUER,

    /** The APK content differs from what was attested. */
    CONTENT_MISMATCH,

    /** The APK was signed with a different key than the attested build. */
    SIGNER_MISMATCH,
}

sealed interface VerifyResult {
    data object NoAttestation : VerifyResult
    data class Invalid(val reason: InvalidReason) : VerifyResult
    data class Verified(val provenance: Provenance) : VerifyResult
}

object TrustedIssuers {
    /** Public half of poc/signer/keys/mock_samsung_demo_private_key.pem. */
    const val MOCK_SAMSUNG_SPKI_BASE64 =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEX7ZRak9LZslVQdEHE3M4qWE947gB" +
            "sJ5pDCxqHHatQkXjD49bva3TOqEqQkoqSgtQeLmwE96NflrKuDXcFcWQfw=="

    val mockSamsung: PublicKey by lazy { decodeEcPublicKey(MOCK_SAMSUNG_SPKI_BASE64) }

    fun decodeEcPublicKey(spkiBase64: String): PublicKey =
        KeyFactory.getInstance("EC")
            .generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(spkiBase64)))
}

/**
 * Checks the provenance attestation stored in an APK's signing block.
 *
 * The signer certificate is read from the signing block without checking the
 * APK signature itself; Android does that when the APK is installed.
 */
object ApkProvenanceVerifier {
    const val STATEMENT_TYPE = "mock-samsung-provenance/v1"

    fun verify(
        apk: File,
        trustedKeys: Collection<PublicKey> = listOf(TrustedIssuers.mockSamsung),
    ): VerifyResult {
        RandomAccessFile(apk, "r").use { file ->
            val layout = try {
                ApkLayout.read(file)
            } catch (e: ApkFormatException) {
                return VerifyResult.NoAttestation
            }
            val envelopeBytes = layout.pair(ApkLayout.PROVENANCE_PAIR_ID)
                ?: return VerifyResult.NoAttestation

            val payload: ByteArray
            val signature: ByteArray
            try {
                val envelope = JSONObject(String(envelopeBytes, Charsets.UTF_8))
                payload = Base64.getDecoder().decode(envelope.getString("statement"))
                signature = Base64.getDecoder().decode(envelope.getString("signature"))
            } catch (e: JSONException) {
                return VerifyResult.Invalid(InvalidReason.MALFORMED)
            } catch (e: IllegalArgumentException) {
                return VerifyResult.Invalid(InvalidReason.MALFORMED)
            }

            if (trustedKeys.none { isSignedBy(it, payload, signature) }) {
                return VerifyResult.Invalid(InvalidReason.UNTRUSTED_ISSUER)
            }

            val provenance: Provenance
            val contentDigest: String
            val signerCertDigest: String
            try {
                val statement = JSONObject(String(payload, Charsets.UTF_8))
                if (statement.getString("type") != STATEMENT_TYPE) {
                    return VerifyResult.Invalid(InvalidReason.MALFORMED)
                }
                val subject = statement.getJSONObject("subject")
                contentDigest = subject.getString("contentDigest")
                signerCertDigest = subject.getString("signerCertDigest")
                val source = statement.getJSONObject("source")
                val build = statement.getJSONObject("build")
                val inputs = statement.optJSONObject("inputs")
                val hosts = inputs?.getJSONArray("networkHosts")
                provenance = Provenance(
                    issuer = statement.getString("issuer"),
                    issuedAt = statement.getString("issuedAt"),
                    repository = source.getString("repository"),
                    repositoryId = source.getString("repositoryId"),
                    owner = source.getString("owner"),
                    ownerId = source.getString("ownerId"),
                    visibility = source.optString("visibility"),
                    commit = source.getString("commit"),
                    ref = source.optString("ref"),
                    workflowRef = build.optString("workflowRef"),
                    runnerEnvironment = build.optString("runnerEnvironment"),
                    runUrl = build.optString("runUrl"),
                    buildHosts = hosts?.let { array -> List(array.length()) { array.getString(it) } },
                    inputArtifactCount = inputs?.optInt("artifactCount") ?: 0,
                )
            } catch (e: JSONException) {
                return VerifyResult.Invalid(InvalidReason.MALFORMED)
            }

            if (contentDigest != "sha256:" + layout.contentDigest(file)) {
                return VerifyResult.Invalid(InvalidReason.CONTENT_MISMATCH)
            }
            val actualSigner = try {
                layout.signerCertDigest()
            } catch (e: ApkFormatException) {
                return VerifyResult.Invalid(InvalidReason.SIGNER_MISMATCH)
            }
            if (signerCertDigest != "sha256:$actualSigner") {
                return VerifyResult.Invalid(InvalidReason.SIGNER_MISMATCH)
            }
            return VerifyResult.Verified(provenance)
        }
    }

    private fun isSignedBy(key: PublicKey, payload: ByteArray, signature: ByteArray): Boolean =
        try {
            Signature.getInstance("SHA256withECDSA").run {
                initVerify(key)
                update(payload)
                verify(signature)
            }
        } catch (e: java.security.GeneralSecurityException) {
            false
        }
}

class ApkFormatException(message: String) : Exception(message)

/** Section offsets of a signed APK plus the pairs of its signing block. */
internal class ApkLayout private constructor(
    private val signingBlockStart: Long,
    private val centralDirStart: Long,
    private val eocdStart: Long,
    private val eocd: ByteArray,
    private val pairs: List<Pair<Int, ByteArray>>,
) {
    fun pair(id: Int): ByteArray? = pairs.firstOrNull { it.first == id }?.second

    /**
     * SHA-256 over everything except the signing block: ZIP entries, central
     * directory and EOCD (with its central directory offset zeroed, since the
     * offset moves when the signing block changes size). Sections are
     * length-prefixed so their boundaries are part of the digest.
     */
    fun contentDigest(file: RandomAccessFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.updateSection(file, 0, signingBlockStart)
        digest.updateSection(file, centralDirStart, eocdStart - centralDirStart)
        val normalizedEocd = eocd.copyOf()
        normalizedEocd.fill(0, EOCD_CD_OFFSET_FIELD, EOCD_CD_OFFSET_FIELD + 4)
        digest.update(u64(normalizedEocd.size.toLong()))
        digest.update(normalizedEocd)
        return digest.digest().toHex()
    }

    /** SHA-256 of the first signer's certificate (v3 block if present, else v2). */
    fun signerCertDigest(): String {
        val block = pair(V3_PAIR_ID) ?: pair(V2_PAIR_ID)
            ?: throw ApkFormatException("no v2 or v3 signature")
        val signers = littleEndian(block).prefixed()
        val signer = signers.prefixed()
        val signedData = signer.prefixed()
        signedData.prefixed() // digests
        val certificates = signedData.prefixed()
        val certificate = certificates.prefixed()
        val der = ByteArray(certificate.remaining()).also { certificate.get(it) }
        return MessageDigest.getInstance("SHA-256").digest(der).toHex()
    }

    companion object {
        const val PROVENANCE_PAIR_ID = 0x50524F56 // "PROV"
        const val V2_PAIR_ID = 0x7109871A
        const val V3_PAIR_ID = 0xF05368C0.toInt()

        private const val EOCD_SIGNATURE = 0x06054B50
        private const val EOCD_MIN_SIZE = 22
        private const val EOCD_CD_OFFSET_FIELD = 16
        private const val MAX_SIGNING_BLOCK_SIZE = 64L * 1024 * 1024
        private val MAGIC = "APK Sig Block 42".toByteArray(Charsets.US_ASCII)

        fun read(file: RandomAccessFile): ApkLayout {
            val length = file.length()
            if (length < EOCD_MIN_SIZE) throw ApkFormatException("file is too short")
            val tailSize = minOf(length, (EOCD_MIN_SIZE + 0xFFFF).toLong()).toInt()
            val tail = ByteArray(tailSize)
            file.seek(length - tailSize)
            file.readFully(tail)
            val tailBuf = littleEndian(tail)

            var eocdInTail = -1
            for (pos in tailSize - EOCD_MIN_SIZE downTo 0) {
                if (tailBuf.getInt(pos) == EOCD_SIGNATURE) {
                    val commentLength = tailBuf.getShort(pos + 20).toInt() and 0xFFFF
                    if (pos + EOCD_MIN_SIZE + commentLength == tailSize) {
                        eocdInTail = pos
                        break
                    }
                }
            }
            if (eocdInTail < 0) throw ApkFormatException("EOCD record not found")
            val eocdStart = length - tailSize + eocdInTail
            val eocd = tail.copyOfRange(eocdInTail, tailSize)
            val cdSize = tailBuf.getInt(eocdInTail + 12).toLong() and 0xFFFFFFFFL
            val cdStart = tailBuf.getInt(eocdInTail + 16).toLong() and 0xFFFFFFFFL
            if (cdStart == 0xFFFFFFFFL) throw ApkFormatException("ZIP64 is not supported")
            if (cdStart + cdSize != eocdStart) {
                throw ApkFormatException("central directory does not end at the EOCD record")
            }
            if (cdStart < 32) throw ApkFormatException("no APK Signing Block")

            val footer = ByteArray(24)
            file.seek(cdStart - 24)
            file.readFully(footer)
            if (!footer.copyOfRange(8, 24).contentEquals(MAGIC)) {
                throw ApkFormatException("no APK Signing Block")
            }
            val blockSize = littleEndian(footer).getLong(0)
            if (blockSize < 24 || blockSize > MAX_SIGNING_BLOCK_SIZE) {
                throw ApkFormatException("signing block size is out of range")
            }
            val blockStart = cdStart - blockSize - 8
            if (blockStart < 0) throw ApkFormatException("signing block size is out of range")

            val block = ByteArray((blockSize + 8).toInt())
            file.seek(blockStart)
            file.readFully(block)
            val buf = littleEndian(block)
            if (buf.getLong(0) != blockSize) {
                throw ApkFormatException("signing block header and footer sizes differ")
            }

            val pairs = mutableListOf<Pair<Int, ByteArray>>()
            var pos = 8
            val end = block.size - 24
            while (pos < end) {
                if (pos + 12 > end) throw ApkFormatException("truncated signing block pair")
                val pairLength = buf.getLong(pos)
                if (pairLength < 4 || pairLength > end - pos - 8) {
                    throw ApkFormatException("signing block pair length is out of range")
                }
                val id = buf.getInt(pos + 8)
                pairs += id to block.copyOfRange(pos + 12, pos + 8 + pairLength.toInt())
                pos += 8 + pairLength.toInt()
            }
            return ApkLayout(blockStart, cdStart, eocdStart, eocd, pairs)
        }

        private fun littleEndian(bytes: ByteArray): ByteBuffer =
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        private fun u64(value: Long): ByteArray =
            ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array()

        private fun MessageDigest.updateSection(file: RandomAccessFile, start: Long, size: Long) {
            update(u64(size))
            val chunk = ByteArray(64 * 1024)
            var remaining = size
            file.seek(start)
            while (remaining > 0) {
                val n = minOf(remaining, chunk.size.toLong()).toInt()
                file.readFully(chunk, 0, n)
                update(chunk, 0, n)
                remaining -= n
            }
        }

        /** Reads a uint32 length and returns a view of that many following bytes. */
        private fun ByteBuffer.prefixed(): ByteBuffer {
            if (remaining() < 4) throw ApkFormatException("truncated length-prefixed field")
            val size = int
            if (size < 0 || size > remaining()) {
                throw ApkFormatException("length-prefixed field is out of range")
            }
            val view = slice().order(ByteOrder.LITTLE_ENDIAN)
            view.limit(size)
            position(position() + size)
            return view
        }

        private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
    }
}
