package com.example.autochindy.data.remote.openai

import com.example.autochindy.data.remote.PromptBuilder
import com.example.autochindy.domain.model.BulletinResult
import com.example.autochindy.domain.model.ConfidenceLevel
import com.example.autochindy.domain.model.EditorialConfig
import com.example.autochindy.domain.repository.BulletinGeneratorRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenAiBulletinGenerator(
    private val apiService: OpenAiApiService,
    private val apiKey: String // Will be injected or retrieved from SecureStorage
) : BulletinGeneratorRepository {

    private val gson = Gson()

    override suspend fun generateBulletin(
        transcriptionText: String,
        config: EditorialConfig,
        regenerateTitleOnly: Boolean,
        regenerateSubtitleOnly: Boolean,
        previousResult: BulletinResult?
    ): Result<BulletinResult> = withContext(Dispatchers.IO) {
        try {
            val systemMsg = Message(role = "system", content = PromptBuilder.buildSystemPrompt())
            val userMsg = Message(
                role = "user",
                content = PromptBuilder.buildUserPrompt(
                    transcriptionText, config, regenerateTitleOnly, regenerateSubtitleOnly
                )
            )

            // If we are regenerating only a specific part, we don't need strict JSON
            val isPartialRegeneration = regenerateTitleOnly || regenerateSubtitleOnly
            val format = if (!isPartialRegeneration) ResponseFormat(type = "json_object") else null

            val request = ChatCompletionRequest(
                model = "gpt-4o",
                temperature = 0.0, // ESTRICTO: Sin lugar a creatividad no deseada.
                responseFormat = format,
                messages = listOf(systemMsg, userMsg)
            )

            val response = apiService.createChatCompletion("Bearer $apiKey", request)
            val responseContent = response.choices.firstOrNull()?.message?.content 
                ?: throw IllegalStateException("Empty response from AI")

            if (isPartialRegeneration && previousResult != null) {
                // Return original bulletin with updated property
                val cleanContent = responseContent.trim().removeSurrounding("\"")
                Result.success(
                    previousResult.copy(
                        title = if (regenerateTitleOnly) cleanContent else previousResult.title,
                        subtitle = if (regenerateSubtitleOnly) cleanContent else previousResult.subtitle
                    )
                )
            } else {
                // Complete JSON Parsing
                val jsonResponse = gson.fromJson(responseContent, JsonBulletinResponse::class.java)
                val mappedConfidence = when (jsonResponse.confidence.uppercase()) {
                    "HIGH" -> ConfidenceLevel.HIGH
                    "MEDIUM" -> ConfidenceLevel.MEDIUM
                    "LOW" -> ConfidenceLevel.LOW
                    else -> ConfidenceLevel.MEDIUM
                }

                Result.success(
                    BulletinResult(
                        title = jsonResponse.title,
                        subtitle = jsonResponse.subtitle,
                        content = jsonResponse.content,
                        confidence = mappedConfidence,
                        rawTranscription = transcriptionText
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
