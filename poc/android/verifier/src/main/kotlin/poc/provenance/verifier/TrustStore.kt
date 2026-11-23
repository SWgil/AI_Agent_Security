package poc.provenance.verifier

import android.content.Context

/**
 * Repositories the user has registered as trusted, keyed by GitHub's numeric
 * repository id. The name is kept only for display.
 */
class TrustStore(context: Context) {
    private val prefs = context.getSharedPreferences("trusted_repositories", Context.MODE_PRIVATE)

    fun isTrusted(repositoryId: String): Boolean = prefs.contains(repositoryId)

    fun trust(repositoryId: String, name: String) = prefs.edit().putString(repositoryId, name).apply()

    fun untrust(repositoryId: String) = prefs.edit().remove(repositoryId).apply()

    /** Registered names, sorted. */
    fun names(): List<String> = prefs.all.values.map { it.toString() }.sorted()
}
