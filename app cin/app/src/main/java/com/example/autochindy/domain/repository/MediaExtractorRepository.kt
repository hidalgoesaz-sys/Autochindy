package com.example.autochindy.domain.repository

import com.example.autochindy.domain.model.AudioSegment
import kotlinx.coroutines.flow.Flow
import java.io.File

sealed class ExtractionProgress {
    object Starting : ExtractionProgress()
    data class Processing(val percent: Int) : ExtractionProgress()
    data class Finished(val segments: List<AudioSegment>) : ExtractionProgress()
    data class Error(val exception: Exception) : ExtractionProgress()
}

interface MediaExtractorRepository {
    /**
     * Extrae audio de un video (o toma un audio) y lo segmenta en bloques.
     * Soporta archivos de cualquier tamaño al devolver un Flow de progreso en %
     * hasta entregar la lista final de segmentos procesados temporalmente.
     */
    fun extractAndSegmentAudio(inputFile: File): Flow<ExtractionProgress>
}
