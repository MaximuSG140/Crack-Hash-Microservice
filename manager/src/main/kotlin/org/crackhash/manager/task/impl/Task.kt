package org.crackhash.manager.task.impl

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.crackhash.manager.task.api.contracts.CreateTaskRequest
import org.crackhash.manager.task.api.contracts.TaskStatus
import org.crackhash.manager.task.api.contracts.CompletedSubtaskEvent
import org.crackhash.manager.task.config.TaskConfigurationProperties
import java.util.*
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@Serializable
data class Task(
    @Required val id: String = UUID.randomUUID().toString(),
    @Required val words: Set<String> = emptySet(),
    @Required val partNumbers: Set<Int> = emptySet(),
    @Required val status: TaskStatus = TaskStatus.CREATED,
    @Required val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val alphabet: String,
    val duration: Duration,
    val hash: String,
    val maxLength: Int,
    val partCount: Int
) {
    companion object {
        fun create(request: CreateTaskRequest, properties: TaskConfigurationProperties): Task =
            run {
                require(request.maxLength > 0) { "Max length=${request.maxLength} must be more than zero" }
                require(request.hash.isNotEmpty()) { "Hash=${request.hash} must not be empty" }
                Task(
                    alphabet = properties.alphabet,
                    duration = properties.ttl.toKotlinDuration(),
                    hash = request.hash,
                    maxLength = request.maxLength,
                    partCount = properties.partCount
                )
            }
    }

    fun update(event: CompletedSubtaskEvent): Task =
        run {
            require(id == event.id) { "Task id=$id not equals request id=${event.id}" }
            if (status == TaskStatus.ERROR) {
                return this
            }
            if (partNumbers.contains(event.partNumber)) { this }
            else {
                val newPartNumbers = partNumbers + event.partNumber
                this.copy(
                    words = words + event.words,
                    partNumbers = newPartNumbers,
                    status = if (partCount > newPartNumbers.size) status else TaskStatus.READY
                )
            }
        }

    fun updateStatus(status: TaskStatus): Task =
        when(status) {
            TaskStatus.IN_PROGRESS -> this.copy(status = TaskStatus.IN_PROGRESS)
            TaskStatus.ERROR -> this.copy(status = TaskStatus.ERROR)
            else -> throw IllegalArgumentException("Unknown status $status")
        }

    fun isExpired(): Boolean =
        status != TaskStatus.READY && Clock.System.now().minus(duration).toLocalDateTime(TimeZone.UTC) > createTime
}
