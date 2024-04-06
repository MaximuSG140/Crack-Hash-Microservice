package org.crackhash.manager.task.api.response

data class TaskResponse(
    val status: TaskStatus,
    val words: List<String>
)
