package poc.provenance.core

import java.io.IOException
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RepoSignalsTest {
    private val now = Instant.parse("2026-11-23T00:00:00Z")
    private val provenance = Provenance(
        issuer = "test", issuedAt = "2026-11-01T00:00:00Z",
        repository = "alice/notes-app", repositoryId = "1001", owner = "alice", ownerId = "2002",
        visibility = "public", commit = "a".repeat(40), ref = "refs/heads/main",
        workflowRef = "", runnerEnvironment = "github-hosted", runUrl = "",
    )
    private val established = RepoInfo(
        fullName = "alice/notes-app", ownerId = "2002",
        createdAt = Instant.parse("2024-01-01T00:00:00Z"),
        ownerCreatedAt = Instant.parse("2020-01-01T00:00:00Z"),
        isFork = false, parentFullName = null,
    )

    private fun kinds(lookup: RepoLookup, p: Provenance = provenance) =
        RepoSignals.assess(p, lookup, now).map { it.kind }

    @Test
    fun `established public repository raises nothing`() {
        assertEquals(emptyList(), kinds(RepoLookup.Found(established)))
    }

    @Test
    fun `new repository and new account are flagged with their age`() {
        val info = established.copy(
            createdAt = Instant.parse("2026-11-20T00:00:00Z"),
            ownerCreatedAt = Instant.parse("2026-11-13T00:00:00Z"),
        )
        val signals = RepoSignals.assess(provenance, RepoLookup.Found(info), now)
        assertEquals(Signal(SignalKind.NEW_REPO, Severity.WARNING, "3"), signals[0])
        assertEquals(Signal(SignalKind.NEW_ACCOUNT, Severity.WARNING, "10"), signals[1])
    }

    @Test
    fun `repository now owned by another account is dangerous`() {
        val signals = RepoSignals.assess(provenance, RepoLookup.Found(established.copy(ownerId = "9999")), now)
        assertEquals(listOf(Signal(SignalKind.OWNER_CHANGED, Severity.DANGER)), signals)
    }

    @Test
    fun `rename and fork are reported with the current names`() {
        val info = established.copy(fullName = "alice/renamed", isFork = true, parentFullName = "bob/original")
        val signals = RepoSignals.assess(provenance, RepoLookup.Found(info), now)
        assertEquals(
            listOf(
                Signal(SignalKind.REPO_RENAMED, Severity.WARNING, "alice/renamed"),
                Signal(SignalKind.FORK, Severity.INFO, "bob/original"),
            ),
            signals,
        )
    }

    @Test
    fun `deleted repository and failed lookup are distinguished`() {
        assertEquals(listOf(SignalKind.REPO_GONE), kinds(RepoLookup.Gone))
        assertEquals(listOf(SignalKind.LOOKUP_FAILED), kinds(RepoLookup.Unavailable("offline")))
    }

    @Test
    fun `private source and brand-like names are flagged`() {
        val p = provenance.copy(repository = "kb-bank-official/kbstar", visibility = "private")
        assertEquals(
            listOf(SignalKind.NOT_PUBLIC, SignalKind.BRAND_KEYWORD),
            kinds(RepoLookup.Found(established.copy(fullName = "kb-bank-official/kbstar")), p),
        )
    }

    @Test
    fun `lookup parses the GitHub responses`() {
        val lookup = GitHubLookup { url ->
            when (url) {
                "https://api.github.com/repositories/1001" -> HttpResult(
                    200,
                    """{"full_name":"alice/notes-app","created_at":"2024-01-01T00:00:00Z","fork":true,
                       "owner":{"id":2002},"parent":{"full_name":"bob/original"}}""",
                )
                "https://api.github.com/user/2002" -> HttpResult(200, """{"created_at":"2020-01-01T00:00:00Z"}""")
                else -> error("unexpected url $url")
            }
        }
        val info = assertIs<RepoLookup.Found>(lookup.lookup("1001")).info
        assertEquals(established.copy(isFork = true, parentFullName = "bob/original"), info)
    }

    @Test
    fun `lookup maps not-found, errors and bad ids`() {
        assertEquals(RepoLookup.Gone, GitHubLookup { HttpResult(404, "") }.lookup("1001"))
        assertIs<RepoLookup.Unavailable>(GitHubLookup { HttpResult(403, "") }.lookup("1001"))
        assertIs<RepoLookup.Unavailable>(GitHubLookup { throw IOException("offline") }.lookup("1001"))
        assertEquals(RepoLookup.Gone, GitHubLookup { error("must not be called") }.lookup("../user"))
    }
}
