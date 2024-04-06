package org.crackhash.worker.subtask.api.event

data class CompletedSubtaskEvent(
    val id: String,
    val partNumber: Int,
    val words: List<String>
)