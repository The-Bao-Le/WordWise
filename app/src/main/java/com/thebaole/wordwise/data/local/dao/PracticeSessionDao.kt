package com.thebaole.wordwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.thebaole.wordwise.data.local.entity.PracticeSessionEntity
import com.thebaole.wordwise.data.local.model.PracticeSessionWithAttempts
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeSessionDao {

    @Insert
    suspend fun insertSession(
        session: PracticeSessionEntity
    ): Long

    @Update
    suspend fun updateSession(
        session: PracticeSessionEntity
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM practice_sessions
        WHERE completed_at IS NOT NULL
        """
    )
    fun observeCompletedSessionCount(): Flow<Int>

    @Transaction
    @Query(
        """
        SELECT *
        FROM practice_sessions
        WHERE session_id = :sessionId
        """
    )
    fun observeSessionWithAttempts(
        sessionId: Long
    ): Flow<PracticeSessionWithAttempts?>

    @Query(
        """
        DELETE FROM practice_sessions
        WHERE session_id = :sessionId
        """
    )
    suspend fun deleteSessionById(sessionId: Long)
}