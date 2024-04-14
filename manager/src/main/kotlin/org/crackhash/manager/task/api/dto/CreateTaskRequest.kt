package org.crackhash.manager.task.api.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class CreateTaskRequest(
    @field:NotEmpty(message = "error.task.request.hash") val hash: String,
    @field:Positive(message = "error.task.request.max_length") val maxLength: Int
)
