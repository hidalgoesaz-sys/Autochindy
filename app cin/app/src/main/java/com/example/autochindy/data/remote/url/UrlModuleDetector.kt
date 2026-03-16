package com.example.autochindy.data.remote.url

import com.example.autochindy.domain.model.UrlSupportStatus
import com.example.autochindy.domain.repository.UrlAudioProvider

/**
 * Acts as a Factory / Router to find the appropriate tool to extract
 * audio based on the public URL provided by the user.
 */
class UrlModuleDetector(
    private val providers: List<UrlAudioProvider>
) {

    /**
     * Resolves the correct platform provider for the URL string.
     * Throws an exception or returns null if it's completely unsupported.
     */
    fun getProviderForUrl(url: String): UrlAudioProvider? {
        return providers.firstOrNull { it.canHandle(url) }
    }

    /**
     * Quick health-check shortcut before UI pushes to Loading Screen.
     */
    suspend fun checkSupportLevel(url: String): UrlSupportStatus {
        val provider = getProviderForUrl(url) ?: return UrlSupportStatus.ERROR
        return provider.checkAvailability(url)
    }
}
