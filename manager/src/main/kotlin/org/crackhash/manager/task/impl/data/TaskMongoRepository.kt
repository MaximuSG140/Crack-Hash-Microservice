package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.api.exception.TaskNotFoundException
import org.crackhash.manager.task.impl.Task
import org.crackhash.manager.util.LogAfter
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Repository
class TaskMongoRepository(
    private val template: ReactiveRedisTemplate<String, Task>,
    private val dao: TaskMongoDao
): TaskRepository {

    @LogAfter
    override fun add(task: Task): Mono<Task> =
        Mono.zip(dao.save(task), template.opsForValue().set(task.id, task, Duration.ofSeconds(30)))
            .map { it.t1 }

    @LogAfter
    override fun remove(task: Task): Mono<Task> =
        Mono.zip(dao.deleteById(task.id).thenReturn(task), template.opsForValue().delete(task.id))
            .map { it.t1 }

    @LogAfter
    override fun findById(id: String): Mono<Task> =
        template.opsForValue().get(id)
            .switchIfEmpty { dao.findById(id).onErrorMap { TaskNotFoundException(id) } }
}