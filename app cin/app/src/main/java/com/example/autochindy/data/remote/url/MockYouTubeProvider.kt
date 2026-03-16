package com.example.autochindy.data.remote.url

import com.example.autochindy.domain.model.UrlSupportStatus
import com.example.autochindy.domain.repository.ExtractionProgress
import com.example.autochindy.domain.repository.UrlAudioProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockYouTubeProvider : UrlAudioProvider {
    override val providerName = "YOUTUBE"

    override fun canHandle(url: String): Boolean {
        return url.contains("youtube.com") || url.contains("youtu.be")
    }

    override suspend fun checkAvailability(url: String): UrlSupportStatus {
        // Mock simulation: Sometimes a video is age-restricted (limitations) or IP banned
        return UrlSupportStatus.COMPATIBLE
    }

    override fun extractAudio(url: String): Flow<ExtractionProgress> = flow {
        emit(ExtractionProgress.Starting)
        // Similar flow as local mock extractor...
    }
}
