package com.example.autochindy.domain.model

enum class SourceType { URL, LOCAL_FILE }

enum class SessionStatus { PENDING, TRANSCRIBING, GENERATING, COMPLETED, FAILED }

data class RecordSession(
    val id: String,
    val dateCreated: Long,
    val sourceType: SourceType,
    val sourceReference: String,
    val editorialInstructions: String?,
    val prioritizePeople: Boolean,
    val transcriptionText: String?,
    val bulletinTitle: String?,
    val bulletinSubtitle: String?,
    val bulletinContent: String?,
    val status: SessionStatus
)
