package com.thebaole.wordwise.data.local.model

import androidx.room.ColumnInfo

data class AttemptStatistics(
    @ColumnInfo(name = "total_attempts")
    val totalAttempts: Long,

    @ColumnInfo(name = "correct_attempts")
    val correctAttempts: Long
)