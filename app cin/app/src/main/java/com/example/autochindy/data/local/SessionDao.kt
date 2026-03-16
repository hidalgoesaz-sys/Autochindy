package com.example.autochindy.data.local

import androidx.room.*
import com.example.autochindy.domain.model.RecordSession
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "record_sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val dateCreated: Long,
    val sourceType: String,
    val sourceReference: String,
    val editorialInstructions: String?,
    val prioritizePeople: Boolean,
    val transcriptionText: String?,
    val bulletinTitle: String?,
    val bulletinSubtitle: String?,
    val bulletinContent: String?,
    val status: String
)

@Dao
interface SessionDao {
    @Query("SELECT * FROM record_sessions ORDER BY dateCreated DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM record_sessions WHERE id = :id")
    suspend fun getSessionById(id: String): SessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)
}
