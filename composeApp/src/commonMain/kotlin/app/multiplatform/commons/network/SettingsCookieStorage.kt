package app.multiplatform.commons.network

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A [CookiesStorage] that persists session cookies across app restarts using
 * [Settings] (multiplatform-settings). Without this, logging in successfully
 * but then killing the app results in a mustbeloggedin error on the next launch
 * because [io.ktor.client.plugins.cookies.AcceptAllCookiesStorage] is in-memory only.
 */
class SettingsCookieStorage(private val settings: Settings) : CookiesStorage {

    private val mutex = Mutex()
    // host -> (cookieName -> Cookie)
    private val cache = mutableMapOf<String, MutableMap<String, Cookie>>()

    private val json = Json { ignoreUnknownKeys = true }
    private val settingsKey = "session_cookies_v1"

    init {
        val raw = settings.getStringOrNull(settingsKey)
        if (raw != null) {
            try {
                json.decodeFromString<List<CookieDto>>(raw).forEach { dto ->
                    cache.getOrPut(dto.host) { mutableMapOf() }[dto.name] = dto.toCookie()
                }
            } catch (_: Exception) {
                // Corrupt data — start fresh so the user just has to log in once more.
                settings.remove(settingsKey)
            }
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        cache.entries
            .filter { (host, _) -> requestUrl.host == host || requestUrl.host.endsWith(".$host") }
            .flatMap { (_, cookies) -> cookies.values }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        val host = cookie.domain ?: requestUrl.host
        cache.getOrPut(host) { mutableMapOf() }[cookie.name] = cookie
        persist()
    }

    override fun close() { /* nothing to close */ }

    /** Clears all stored cookies (call on logout). */
    suspend fun clear(): Unit = mutex.withLock {
        cache.clear()
        settings.remove(settingsKey)
    }

    private fun persist() {
        val dtos = cache.flatMap { (host, cookies) ->
            cookies.values.map { CookieDto.from(host, it) }
        }
        settings[settingsKey] = json.encodeToString(dtos)
    }

    @Serializable
    private data class CookieDto(
        val host: String,
        val name: String,
        val value: String,
        val domain: String? = null,
        val path: String? = null,
        val secure: Boolean = false,
        val httpOnly: Boolean = false,
        val maxAge: Int? = null,
    ) {
        fun toCookie() = Cookie(
            name = name,
            value = value,
            domain = domain,
            path = path,
            secure = secure,
            httpOnly = httpOnly,
            maxAge = maxAge,
        )

        companion object {
            fun from(host: String, c: Cookie) = CookieDto(
                host = host,
                name = c.name,
                value = c.value,
                domain = c.domain,
                path = c.path,
                secure = c.secure,
                httpOnly = c.httpOnly,
                maxAge = c.maxAge,
            )
        }
    }
}

