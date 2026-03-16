package com.example.autochindy.data.remote.url

import com.example.autochindy.domain.model.UrlSupportStatus
import com.example.autochindy.domain.repository.ExtractionProgress
import com.example.autochindy.domain.repository.UrlAudioProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockFacebookProvider : UrlAudioProvider {
    override val providerName = "FACEBOOK"

    override fun canHandle(url: String): Boolean {
        val lowerUrl = url.lowercase()
        return lowerUrl.contains("facebook.com") || lowerUrl.contains("fb.watch")
    }

    override suspend fun checkAvailability(url: String): UrlSupportStatus {
        // Facebook reels often have low quality mono audio extraction
        return UrlSupportStatus.COMPATIBLE_WITH_LIMITATIONS
    }

    override fun extractAudio(url: String): Flow<ExtractionProgress> = flow {
        emit(ExtractionProgress.Starting)
        // Similar flow as local mock extractor...
    }
}
