package com.thebaole.wordwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thebaole.wordwise.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWords(
        words: List<WordEntity>
    ): List<Long>

    @Query(
        """
        SELECT COUNT(*)
        FROM words
        WHERE next_review_at <= :now
        """
    )
    fun observeDueWordCount(now: Long): Flow<Int>

    @Query(
        """
        SELECT COUNT(*)
        FROM words
        WHERE is_mastered = 1
        """
    )
    fun observeMasteredWordCount(): Flow<Int>

    @Query(
        """
        SELECT *
        FROM words
        ORDER BY
            CASE
                WHEN next_review_at <= :now THEN 0
                ELSE 1
            END,
            next_review_at ASC,
            RANDOM()
        LIMIT :limit
        """
    )
    suspend fun getWordsForSession(
        now: Long,
        limit: Int
    ): List<WordEntity>

    @Query(
        """
        UPDATE words
        SET
            next_review_at = :nextReviewAt,
            consecutive_correct = :consecutiveCorrect,
            is_mastered = :isMastered
        WHERE word_id = :wordId
        """
    )
    suspend fun updateLearningProgress(
        wordId: Long,
        nextReviewAt: Long,
        consecutiveCorrect: Int,
        isMastered: Boolean
    )

    @Query(
        """
    SELECT *
    FROM words
    WHERE word_id = :wordId
    LIMIT 1
    """
    )
    suspend fun getWordById(
        wordId: Long
    ): WordEntity?

    @Query(
        """
    SELECT *
    FROM words
    ORDER BY word_id ASC
    """
    )
    suspend fun getAllWords(): List<WordEntity>

    @Query(
        """
    UPDATE words
    SET next_review_at = 0,
        consecutive_correct = 0,
        is_mastered = 0
    """
    )
    suspend fun resetLearningProgress()
}