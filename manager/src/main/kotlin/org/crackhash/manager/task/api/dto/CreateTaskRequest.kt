package org.crackhash.manager.task.api.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class CreateTaskRequest(
    @NotEmpty(message = "Hash may not be empty")
    val hash: String,

    @Positive(message = "Max length must not be less than 0")
    val maxLength: Int
)
