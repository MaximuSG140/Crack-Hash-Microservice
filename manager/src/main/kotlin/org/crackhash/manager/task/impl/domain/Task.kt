package org.crackhash.manager.task.impl.domain

import org.crackhash.manager.task.api.response.TaskStatus
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.crackhash.manager.task.api.request.CreateTaskRequest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Document("task")
data class Task(
    @Id val id: String = UUID.randomUUID().toString(),
    val words: List<String> = emptyList(),
    val partNumbers: List<Int> = emptyList(),
    val status: TaskStatus = TaskStatus.IN_PROGRESS,
    val createTime: Date = Date.from(ZonedDateTime.now().toInstant()),
    val hash: String,
    val maxLength: Int,
    val partCount: Int
): Serializable {
    companion object {
        fun create(request: CreateTaskRequest, partCount: Int): Task {
            if (request.maxLength <= 0) {
                throw IllegalArgumentException("Max length=${request.maxLength} must be more than zero")
            }
            if (request.hash.isEmpty()) {
                throw IllegalArgumentException("Hash=${request.hash} must not be empty")
            }
            return Task(hash = request.hash, maxLength = request.maxLength, partCount = partCount)
        }
    }

    fun updateByCompletedSubtaskEvent(event: CompletedSubtaskEvent): Task {
        if (id != event.id) {
            throw IllegalArgumentException("Task id=$id not equals request id=${event.id}")
        }
        if (partNumbers.contains(event.partNumber)) {
            return this
        }
        val newPartNumbers = partNumbers + event.partNumber
        return this.copy(
            words = words + event.words,
            partNumbers = newPartNumbers,
            status = if (partCount > newPartNumbers.size) status else TaskStatus.READY
        )
    }

    fun updateByTimeout(timeoutInSecond: Long): Task {
        if (timeoutInSecond <= 0) {
            throw IllegalArgumentException("Timeout in minutes=$timeoutInSecond must be more than zero")
        }
        return this.copy(status = if (isErrorStatus(timeoutInSecond)) TaskStatus.ERROR else status)
    }

    private fun isErrorStatus(timeoutInSecond: Long): Boolean =
        run { status != TaskStatus.READY &&
                ZonedDateTime.now() > createTime.toInstant().atZone(ZoneOffset.UTC).plusSeconds(timeoutInSecond) }
}
