package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.impl.Task
import reactor.core.publisher.Mono

interface TaskRepository {

    fun add(task: Task): Mono<Task>

    fun remove(task: Task): Mono<Task>

    fun findById(id: String): Mono<Task>
}