package org.crackhash.manager.task.api.event

data class CreatedTaskEvent(
    val id: String,
    val partNumber: Int,
    val partCount: Int,
    val hash: String,
    val maxLength: Int,
    val alphabet: String
)
