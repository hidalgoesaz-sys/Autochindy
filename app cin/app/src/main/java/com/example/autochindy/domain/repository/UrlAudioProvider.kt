package com.example.autochindy.domain.repository

import com.example.autochindy.domain.model.UrlSupportStatus
import kotlinx.coroutines.flow.Flow

interface UrlAudioProvider {
    val providerName: String // E.g., "YOUTUBE", "FACEBOOK"

    /**
     * Determines if this provider can handle the given URL.
     */
    fun canHandle(url: String): Boolean

    /**
     * Checks if the resource is currently available and extractable.
     * Use this to detect temporary IP bans or region-locked video errors
     * before starting a full audio extraction attempt.
     */
    suspend fun checkAvailability(url: String): UrlSupportStatus

    /**
     * Extracts and normalizes the audio stream into local segments,
     * emitting progress to the UI matching the [MediaExtractorRepository] flow.
     */
    fun extractAudio(url: String): Flow<ExtractionProgress>
}
