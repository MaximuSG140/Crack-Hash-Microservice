package org.crackhash.manager.task.api

import org.crackhash.manager.task.api.contracts.CompletedSubtaskEvent
import org.crackhash.manager.task.api.contracts.CreateTaskRequest
import org.crackhash.manager.task.api.contracts.TaskResponse
import reactor.core.publisher.Mono

interface TaskService {
    fun createTask(request: CreateTaskRequest): String
    fun updateTask(event: CompletedSubtaskEvent): Unit
    fun getTaskResponse(id: String): TaskResponse
}