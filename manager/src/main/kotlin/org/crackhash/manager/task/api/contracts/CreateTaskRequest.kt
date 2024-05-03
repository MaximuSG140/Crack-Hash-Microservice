package org.crackhash.manager.task.api.contracts

data class CreateTaskRequest(
    val hash: String,
    val maxLength: Int
)
