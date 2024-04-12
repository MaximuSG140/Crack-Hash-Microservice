package org.crackhash.manager.task.impl.data

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.crackhash.manager.task.impl.Task
import org.crackhash.manager.util.DomainCacheRepository
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class TaskCacheRepository(
    private val template: ReactiveStringRedisTemplate,
    repository: TaskRepository
): DomainCacheRepository<Task, String>(repository) {

    override fun addInCache(domainModel: Task): Mono<Unit> =
        template.opsForValue()
            .set(domainModel.id, Json.encodeToString(domainModel), Duration.ofSeconds(30))
            .thenReturn(Unit)

    override fun deleteFromCache(domainModel: Task): Mono<Unit> =
        template.opsForValue().delete(domainModel.id).thenReturn(Unit)

    override fun findInCache(id: String): Mono<Task> =
        template.opsForValue().get(id).map { Json.decodeFromString<Task>(it) }
}