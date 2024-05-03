package org.crackhash.manager.task.api.contracts

import kotlinx.serialization.Serializable

@Serializable
data class CompletedSubtaskEvent(
    val id: String,
    val partNumber: Int,
    val words: Set<String>
)