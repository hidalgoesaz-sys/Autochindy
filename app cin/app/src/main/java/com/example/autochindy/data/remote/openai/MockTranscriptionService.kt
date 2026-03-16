package com.example.autochindy.data.remote.openai

import com.example.autochindy.domain.model.AudioSegment
import com.example.autochindy.domain.model.CompleteTranscription
import com.example.autochindy.domain.model.TranscriptionSegment
import com.example.autochindy.domain.repository.TranscriptionProgress
import com.example.autochindy.domain.repository.TranscriptionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockTranscriptionService : TranscriptionRepository {

    override fun transcribeAudioSegments(segments: List<AudioSegment>): Flow<TranscriptionProgress> = flow {
        val transcribedSegments = mutableListOf<TranscriptionSegment>()
        val total = segments.size

        segments.forEachIndexed { index, segment ->
            emit(TranscriptionProgress.SegmentTranscribing(currentSegment = index + 1, totalSegments = total))
            
            delay(1000) // simular la llamada a la API de Whisper
            
            transcribedSegments.add(
                TranscriptionSegment(
                    segmentId = segment.id,
                    startTimeSeconds = segment.startTimeSeconds,
                    endTimeSeconds = segment.endTimeSeconds,
                    text = "[${segment.startTimeSeconds}s] Transcripción del segmento ${segment.id}..."
                )
            )
        }

        val fullText = transcribedSegments.joinToString(separator = "\n") { it.text }
        
        emit(TranscriptionProgress.Finished(
            CompleteTranscription(
                fullText = fullText,
                segments = transcribedSegments
            )
        ))
    }
}
