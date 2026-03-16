package com.example.autochindy.domain.repository

import com.example.autochindy.domain.model.BulletinResult
import com.example.autochindy.domain.model.EditorialConfig

interface BulletinGeneratorRepository {
    /**
     * Genera o regenera un boletín basándose en la transcripción.
     * Si 'regenerateTitleOnly' o 'regenerateSubtitleOnly' son true, el LLM
     * mantendrá el resto intacto y solo re-emitirá esa parte específica.
     */
    suspend fun generateBulletin(
        transcriptionText: String,
        config: EditorialConfig,
        regenerateTitleOnly: Boolean = false,
        regenerateSubtitleOnly: Boolean = false,
        previousResult: BulletinResult? = null
    ): Result<BulletinResult>
}
