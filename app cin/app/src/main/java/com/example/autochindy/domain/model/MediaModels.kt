package com.example.autochindy.domain.model

import java.io.File

data class AudioSegment(
    val id: Int,
    val file: File,
    val startTimeSeconds: Long,
    val endTimeSeconds: Long
)

data class TranscriptionSegment(
    val segmentId: Int,
    val startTimeSeconds: Long,
    val endTimeSeconds: Long,
    val text: String
)

data class CompleteTranscription(
    val fullText: String,
    val segments: List<TranscriptionSegment>
)

enum class BulletinType {
    FULL_COVERAGE,
    SUMMARIZED
}
