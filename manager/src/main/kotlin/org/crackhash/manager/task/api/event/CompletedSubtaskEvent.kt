package org.crackhash.manager.task.api.event

data class CompletedSubtaskEvent(
    val id: String,
    val partNumber: Int,
    val words: List<String>
)