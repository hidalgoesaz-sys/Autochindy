package com.example.autochindy.export

import android.content.Context
import java.io.File

interface DocxExporter {
    /**
     * Creates a .docx file containing the bulletin and/or transcription.
     * Returns the generated File reference, which can then be shared via Intents.
     */
    suspend fun exportToWord(
        context: Context,
        title: String?,
        subtitle: String?,
        content: String?,
        transcription: String?,
        includeBulletin: Boolean,
        includeTranscription: Boolean
    ): Result<File>
}
