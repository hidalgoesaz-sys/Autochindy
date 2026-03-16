package com.example.autochindy.domain.model

enum class PriorityPreference {
    TITLE,
    SUBTITLE,
    BOTH,
    BODY_ONLY,
    NONE
}

data class EditorialConfig(
    val customInstructions: String?,
    val peopleToPrioritize: List<String>,
    val priorityPreference: PriorityPreference,
    val bulletinType: BulletinType = BulletinType.FULL_COVERAGE
)

enum class ConfidenceLevel {
    HIGH, // Transcript was clear, no ambiguous parts
    MEDIUM, // Some parts where unclear but manageable
    LOW // High risk of hallucination or very poor audio quality detected
}

data class BulletinResult(
    val title: String,
    val subtitle: String,
    val content: String,
    val confidence: ConfidenceLevel,
    val rawTranscription: String
)
