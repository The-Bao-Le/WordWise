package com.thebaole.wordwise.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attempts",
    foreignKeys = [
        ForeignKey(
            entity = PracticeSessionEntity::class,
            parentColumns = ["session_id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["word_id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("session_id"),
        Index("word_id")
    ]
)
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "attempt_id")
    val id: Long = 0,

    @ColumnInfo(name = "session_id")
    val sessionId: Long,

    @ColumnInfo(name = "word_id")
    val wordId: Long,

    @ColumnInfo(name = "selected_answer")
    val selectedAnswer: String,

    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean,

    @ColumnInfo(name = "answered_at")
    val answeredAt: Long
)