package org.crackhash.manager.task.api.contracts

data class TaskResponse(
    val status: TaskStatus,
    val words: Set<String>
)
