package com.example.autochindy.domain.repository

import com.example.autochindy.domain.model.RecordSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAllSessions(): Flow<List<RecordSession>>
    suspend fun getSessionById(id: String): RecordSession?
    suspend fun insertSession(session: RecordSession)
    suspend fun updateSession(session: RecordSession)
    suspend fun deleteSession(session: RecordSession)
}
