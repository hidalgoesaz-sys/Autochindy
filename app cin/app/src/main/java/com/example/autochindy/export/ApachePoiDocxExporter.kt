package com.example.autochindy.export

import android.content.Context
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApachePoiDocxExporter : DocxExporter {

    override suspend fun exportToWord(
        context: Context,
        title: String?,
        subtitle: String?,
        content: String?,
        transcription: String?,
        includeBulletin: Boolean,
        includeTranscription: Boolean
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val document = XWPFDocument()

            if (includeBulletin) {
                // Title
                title?.let {
                    val titleParagraph = document.createParagraph()
                    val titleRun = titleParagraph.createRun()
                    titleRun.setText(it)
                    titleRun.isBold = true
                    titleRun.fontSize = 20
                }

                // Subtitle
                subtitle?.let {
                    val subtitleParagraph = document.createParagraph()
                    val subtitleRun = subtitleParagraph.createRun()
                    subtitleRun.setText(it)
                    subtitleRun.isItalic = true
                    subtitleRun.fontSize = 16
                }

                // Content
                content?.let {
                    val contentParagraph = document.createParagraph()
                    val contentRun = contentParagraph.createRun()
                    contentRun.setText(it)
                    contentRun.fontSize = 12
                }
            }

            if (includeBulletin && includeTranscription) {
                document.createParagraph().createRun().addBreak()
                document.createParagraph().createRun().setText("--- TRANSCRIPCIÓN ORIGINAL ---")
            }

            if (includeTranscription) {
                transcription?.let {
                    val transParagraph = document.createParagraph()
                    val transRun = transParagraph.createRun()
                    transRun.setText(it)
                    transRun.fontSize = 10
                }
            }

            val fileDirectory = File(context.cacheDir, "exports")
            if (!fileDirectory.exists()) fileDirectory.mkdirs()
            
            val timestamp = System.currentTimeMillis()
            val file = File(fileDirectory, "Boletin_Autochindy_$timestamp.docx")
            
            FileOutputStream(file).use { out ->
                document.write(out)
            }
            
            document.close()
            Result.success(file)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
