package com.thebaole.wordwise.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    indices = [
        Index(
            value = ["term"],
            unique = true
        )
    ]
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "word_id")
    val id: Long = 0,

    val term: String,

    val definition: String,

    @ColumnInfo(name = "example_sentence")
    val exampleSentence: String,

    val difficulty: String = "foundation",

    @ColumnInfo(name = "next_review_at")
    val nextReviewAt: Long = 0L,

    @ColumnInfo(name = "consecutive_correct")
    val consecutiveCorrect: Int = 0,

    @ColumnInfo(name = "is_mastered")
    val isMastered: Boolean = false
)