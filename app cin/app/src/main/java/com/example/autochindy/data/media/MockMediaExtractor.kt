package com.example.autochindy.data.media

import com.example.autochindy.domain.model.AudioSegment
import com.example.autochindy.domain.repository.ExtractionProgress
import com.example.autochindy.domain.repository.MediaExtractorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class MockMediaExtractor : MediaExtractorRepository {

    override fun extractAndSegmentAudio(inputFile: File): Flow<ExtractionProgress> = flow {
        emit(ExtractionProgress.Starting)
        
        // Simulating the extraction progress of a large video file
        for (i in 1..10) {
            delay(500) // Simulate work
            emit(ExtractionProgress.Processing(percent = i * 10))
        }

        // Mock segments
        val segments = listOf(
            AudioSegment(id = 1, file = File("mock_segment_1.mp3"), startTimeSeconds = 0, endTimeSeconds = 60),
            AudioSegment(id = 2, file = File("mock_segment_2.mp3"), startTimeSeconds = 60, endTimeSeconds = 120),
            AudioSegment(id = 3, file = File("mock_segment_3.mp3"), startTimeSeconds = 120, endTimeSeconds = 180)
        )

        emit(ExtractionProgress.Finished(segments))
    }
}
