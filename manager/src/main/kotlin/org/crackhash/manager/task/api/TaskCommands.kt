package org.crackhash.manager.task.api

import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.crackhash.manager.task.api.request.CreateTaskRequest
import org.crackhash.manager.task.api.response.TaskResponse
import org.springframework.scheduling.annotation.Async

interface TaskCommands {

    fun createTask(request: CreateTaskRequest): String

    @Async
    fun updateTask(event: CompletedSubtaskEvent)

    fun getTaskResponse(id: String): TaskResponse
}