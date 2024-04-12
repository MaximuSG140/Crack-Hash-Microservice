package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.api.exception.TaskNotFoundException
import org.crackhash.manager.task.impl.Task
import org.crackhash.manager.util.DomainRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class TaskRepository(private val template: ReactiveMongoTemplate): DomainRepository<Task, String> {

    override fun add(domainModel: Task): Mono<Task> =
        template.save(domainModel)

    override fun remove(domainModel: Task): Mono<Task> =
        template.remove(domainModel.id).thenReturn(domainModel)

    override fun findById(id: String): Mono<Task> =
        template.findById(id, Task::class.java).onErrorMap { TaskNotFoundException(id) }
}