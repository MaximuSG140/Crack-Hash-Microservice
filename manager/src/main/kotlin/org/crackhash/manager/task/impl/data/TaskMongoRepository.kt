package org.crackhash.manager.task.impl.data

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.crackhash.manager.task.api.exception.TaskNotFoundException
import org.crackhash.manager.task.impl.Task
import org.crackhash.manager.util.LogAfter
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Repository
class TaskMongoRepository(
    private val redisTemplate: ReactiveStringRedisTemplate, private val mongoTemplate: ReactiveMongoTemplate
): TaskRepository {

    @LogAfter
    override fun add(task: Task): Mono<Task> = Mono.zip(
        mongoTemplate.save(task), redisTemplate.opsForValue().set(
            task.id, Json.encodeToString(task), Duration.ofSeconds(30)
        )
    ).map { it.t1 }

    @LogAfter
    override fun remove(task: Task): Mono<Task> = Mono.zip(
        mongoTemplate.remove(task.id).thenReturn(task), redisTemplate.opsForValue().delete(task.id)
    ).map { it.t1 }

    @LogAfter
    override fun findById(id: String): Mono<Task> =
        redisTemplate.opsForValue().get(id).map { Json.decodeFromString<Task>(it) }
            .switchIfEmpty { mongoTemplate.findById(id, Task::class.java).onErrorMap { TaskNotFoundException(id) } }
}