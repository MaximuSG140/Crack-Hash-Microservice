package org.crackhash.manager.task.impl

import org.crackhash.manager.config.ManagerConfigurationProperties
import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.dto.CreateTaskRequest
import org.crackhash.manager.task.api.dto.TaskResponse
import org.crackhash.manager.task.api.dto.TaskStatus
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.crackhash.manager.task.api.event.CreatedTaskEvent
import org.crackhash.manager.task.impl.Task.Companion.create
import org.crackhash.manager.task.impl.data.TaskCacheRepository
import org.crackhash.manager.util.LogBefore
import org.crackhash.manager.util.Sender
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class TaskServiceImpl(
    private val properties: ManagerConfigurationProperties,
    private val repository: TaskCacheRepository,
    private val sender: Sender
): TaskService {

    @LogBefore
    override fun createTask(request: CreateTaskRequest): Mono<String> =
        repository.add(create(request, properties.partCount))
            .publishOn(Schedulers.boundedElastic())
            .doOnNext { Mono.just(sender(mapToCreatedTaskEvents(it))).subscribe() }
            .map { it.id }

    @LogBefore
    override fun updateTask(event: CompletedSubtaskEvent): Mono<Unit> =
        repository.findById(event.id)
            .map { it.updateByCompletedSubtaskEvent(event) }
            .flatMap { repository.add(it) }
            .thenReturn(Unit)

    @LogBefore
    override fun getTaskResponse(id: String): Mono<TaskResponse> =
        repository.findById(id)
            .map { it.updateByTimeout(properties.ttl.toLong()) }
            .flatMap { if (it.status == TaskStatus.ERROR) repository.remove(it) else Mono.just(it) }
            .map { TaskResponse(it.status, it.words) }

    private fun mapToCreatedTaskEvents(task: Task): List<CreatedTaskEvent> =
        List(properties.partCount) { CreatedTaskEvent(task.id, it, properties.partCount, task.hash, task.maxLength, properties.alphabet) }
}