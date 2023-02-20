package com.codemave.core.domain.entity

import java.time.LocalDateTime

data class Payment(
    val paymentId: Int? = 0,
    val title: String,
    val categoryId: Long,
    val priority: Int,
    val date: LocalDateTime
)
