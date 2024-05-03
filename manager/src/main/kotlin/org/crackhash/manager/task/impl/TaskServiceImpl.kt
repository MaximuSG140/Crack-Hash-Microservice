package org.crackhash.manager.task.impl

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.contracts.CreateTaskRequest
import org.crackhash.manager.task.api.contracts.TaskResponse
import org.crackhash.manager.task.api.contracts.TaskStatus
import org.crackhash.manager.task.api.contracts.CompletedSubtaskEvent
import org.crackhash.manager.task.api.contracts.CreatedTaskEvent
import org.crackhash.manager.task.config.TaskConfigurationProperties
import org.crackhash.manager.task.impl.data.DomainRepository
import org.crackhash.manager.task.api.Sender
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class TaskServiceImpl(
    private val properties: TaskConfigurationProperties,
    private val repository: DomainRepository<Task, String>,
    private val sender: Sender
): TaskService {

    override fun createTask(request: CreateTaskRequest): String =
        repository.add(Task.create(request, properties)).id

    override fun updateTask(event: CompletedSubtaskEvent) {
        val task = repository.findById(event.id)?.update(event)
        if (task != null) {
            repository.add(task)
        }
    }

    override fun getTaskResponse(id: String): TaskResponse {
        val task = repository.findById(id) ?: throw IllegalArgumentException("No task found with id: $id")
        if (task.isExpired()) {
            repository.add(task.updateStatus(TaskStatus.ERROR))
        }
        return TaskResponse(task.status, task.words)
    }


    @Async
    @Scheduled(fixedDelay = 20000)
    fun resendCreatedTask() {
        repository.findAll()
            .filter { it.status == TaskStatus.CREATED }
            .forEach { task ->
                sender(toJsonElements(task))
                    .also { repository.add(task.updateStatus(TaskStatus.IN_PROGRESS)) }
        }
    }

    private fun toJsonElements(task: Task): List<JsonElement> =
        List(task.partCount) {
            Json.encodeToJsonElement(
                CreatedTaskEvent(task.id, it, task.partCount, task.hash, task.maxLength, task.alphabet)
            )
        }
}