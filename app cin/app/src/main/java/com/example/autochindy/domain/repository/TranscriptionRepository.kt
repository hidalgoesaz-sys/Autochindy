package com.example.autochindy.domain.repository

import com.example.autochindy.domain.model.AudioSegment
import com.example.autochindy.domain.model.CompleteTranscription
import kotlinx.coroutines.flow.Flow

sealed class TranscriptionProgress {
    data class SegmentTranscribing(val currentSegment: Int, val totalSegments: Int) : TranscriptionProgress()
    data class Finished(val result: CompleteTranscription) : TranscriptionProgress()
    data class Error(val exception: Exception) : TranscriptionProgress()
}

interface TranscriptionRepository {
    /**
     * Transcribe progresivamente una lista de segmentos de audio.
     * Envía progreso segmento a segmento para evitar cuellos de botella y
     * devuelve la transcripción completa incluyendo los timestamps.
     */
    fun transcribeAudioSegments(segments: List<AudioSegment>): Flow<TranscriptionProgress>
}
