package org.crackhash.manager.task.impl.service

import org.crackhash.manager.config.LogBefore
import org.crackhash.manager.config.ManagerConfigurationProperties
import org.crackhash.manager.config.Sender
import org.crackhash.manager.task.api.TaskCommands
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.crackhash.manager.task.api.event.CreatedTaskEvent
import org.crackhash.manager.task.api.request.CreateTaskRequest
import org.crackhash.manager.task.api.response.TaskResponse
import org.crackhash.manager.task.api.response.TaskStatus
import org.crackhash.manager.task.impl.data.TaskRepository
import org.crackhash.manager.task.impl.domain.Task
import org.springframework.stereotype.Service

@Service
class TaskCommandsService(
    private val properties: ManagerConfigurationProperties,
    private val repository: TaskRepository,
    private val sender: Sender
): TaskCommands {

    @LogBefore
    override fun createTask(request: CreateTaskRequest): String =
        runCatching { Task.create(request, properties.partCount) }
            .onSuccess { repository.add(it) }
            .onSuccess { sender(mapToCreatedTaskEvents(it)) }
            .map { it.id }
            .getOrThrow()

    @LogBefore
    override fun updateTask(event: CompletedSubtaskEvent) {
        runCatching { repository.find(event.id) }
            .mapCatching { it.updateByCompletedSubtaskEvent(event) }
            .onSuccess { repository.add(it) }
    }

    @LogBefore
    override fun getTaskResponse(id: String): TaskResponse =
        runCatching { repository.find(id) }
            .mapCatching { it.updateByTimeout(properties.ttl.toLong()) }
            .onSuccess { if (it.status == TaskStatus.ERROR) repository.remove(it) }
            .map { mapToTaskResponse(it) }
            .getOrThrow()

    private fun mapToTaskResponse(task: Task): TaskResponse = TaskResponse(task.status, task.words)

    private fun mapToCreatedTaskEvents(task: Task): List<CreatedTaskEvent> =
        List(properties.partCount) {
            CreatedTaskEvent(task.id, it, properties.partCount, task.hash, task.maxLength, properties.alphabet)
        }
}