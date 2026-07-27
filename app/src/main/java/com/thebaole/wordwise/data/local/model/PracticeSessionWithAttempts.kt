package com.thebaole.wordwise.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.thebaole.wordwise.data.local.entity.AttemptEntity
import com.thebaole.wordwise.data.local.entity.PracticeSessionEntity

data class PracticeSessionWithAttempts(
    @Embedded
    val session: PracticeSessionEntity,

    @Relation(
        parentColumn = "session_id",
        entityColumn = "session_id"
    )
    val attempts: List<AttemptEntity>
)