package com.example.autochindy.data.repository

import com.example.autochindy.data.local.SessionDao
import com.example.autochindy.data.local.SessionEntity
import com.example.autochindy.domain.model.RecordSession
import com.example.autochindy.domain.model.SessionStatus
import com.example.autochindy.domain.model.SourceType
import com.example.autochindy.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override fun getAllSessions(): Flow<List<RecordSession>> {
        return sessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getSessionById(id: String): RecordSession? {
        return sessionDao.getSessionById(id)?.toDomainModel()
    }

    override suspend fun insertSession(session: RecordSession) {
        sessionDao.insertSession(session.toEntity())
    }

    override suspend fun updateSession(session: RecordSession) {
        sessionDao.updateSession(session.toEntity())
    }

    override suspend fun deleteSession(session: RecordSession) {
        sessionDao.deleteSession(session.toEntity())
    }

    // Mappers
    private fun SessionEntity.toDomainModel() = RecordSession(
        id = id,
        dateCreated = dateCreated,
        sourceType = SourceType.valueOf(sourceType),
        sourceReference = sourceReference,
        editorialInstructions = editorialInstructions,
        prioritizePeople = prioritizePeople,
        transcriptionText = transcriptionText,
        bulletinTitle = bulletinTitle,
        bulletinSubtitle = bulletinSubtitle,
        bulletinContent = bulletinContent,
        status = SessionStatus.valueOf(status)
    )

    private fun RecordSession.toEntity() = SessionEntity(
        id = id,
        dateCreated = dateCreated,
        sourceType = sourceType.name,
        sourceReference = sourceReference,
        editorialInstructions = editorialInstructions,
        prioritizePeople = prioritizePeople,
        transcriptionText = transcriptionText,
        bulletinTitle = bulletinTitle,
        bulletinSubtitle = bulletinSubtitle,
        bulletinContent = bulletinContent,
        status = status.name
    )
}
