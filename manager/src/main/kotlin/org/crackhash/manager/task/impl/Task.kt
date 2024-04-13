package org.crackhash.manager.task.impl

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.crackhash.manager.task.api.dto.CreateTaskRequest
import org.crackhash.manager.task.api.dto.TaskStatus
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.time.Duration

@Document("task")
@Serializable
data class Task(
    @Id @Required val id: String = UUID.randomUUID().toString(),
    @Required val words: Set<String> = emptySet(),
    @Required val partNumbers: Set<Int> = emptySet(),
    @Required val status: TaskStatus = TaskStatus.IN_PROGRESS,
    @Required val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val hash: String,
    val maxLength: Int,
    val partCount: Int
) {
    companion object {
        fun create(request: CreateTaskRequest, partCount: Int): Task =
            run {
                require(request.maxLength > 0) { "Max length=${request.maxLength} must be more than zero" }
                require(request.hash.isNotEmpty()) { "Hash=${request.hash} must not be empty" }
                Task(hash = request.hash, maxLength = request.maxLength, partCount = partCount)
            }
    }

    fun updateByCompletedSubtaskEvent(event: CompletedSubtaskEvent): Task =
        run {
            require(id == event.id) { "Task id=$id not equals request id=${event.id}" }
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

    fun updateByTimeout(duration: Duration): Task =
        run {
            require(duration.isPositive()) { "Duration=$duration must be positive" }
            this.copy(status = if (isErrorStatus(duration)) TaskStatus.ERROR else status)
        }

    private fun isErrorStatus(duration: Duration): Boolean =
        status != TaskStatus.READY && Clock.System.now().minus(duration).toLocalDateTime(TimeZone.UTC) > createTime
}
