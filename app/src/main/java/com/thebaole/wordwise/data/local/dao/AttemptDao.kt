package com.thebaole.wordwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thebaole.wordwise.data.local.entity.AttemptEntity
import com.thebaole.wordwise.data.local.model.AttemptStatistics
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {

    @Insert
    suspend fun insertAttempt(
        attempt: AttemptEntity
    ): Long

    @Insert
    suspend fun insertAttempts(
        attempts: List<AttemptEntity>
    )

    @Query(
        """
        SELECT
            COUNT(*) AS total_attempts,
            COALESCE(
                SUM(
                    CASE
                        WHEN is_correct = 1 THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS correct_attempts
        FROM attempts
        """
    )
    fun observeAttemptStatistics(): Flow<AttemptStatistics>

    @Query(
        """
        SELECT COUNT(*)
        FROM attempts
        WHERE session_id = :sessionId
        """
    )
    suspend fun countAttemptsForSession(
        sessionId: Long
    ): Int
}