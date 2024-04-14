package org.crackhash.manager.task.port

import jakarta.validation.Validator
import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.dto.CreateTaskRequest
import org.crackhash.manager.task.api.dto.TaskResponse
import org.crackhash.manager.task.api.exception.TaskException
import org.crackhash.manager.task.api.exception.TaskNotFoundException
import org.crackhash.manager.task.api.exception.TaskRequestException
import org.crackhash.manager.task.config.TaskRoute
import org.crackhash.manager.util.DomainExceptionTranslator
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(TaskRoute.API)
class TaskController(
    private val service: TaskService,
    private val translator: DomainExceptionTranslator,
    private val validator: Validator
) {

    @PostMapping(TaskRoute.CREATE_TASK)
    fun createTask(@RequestBody request: CreateTaskRequest): Mono<ResponseEntity<String>> =
        validate(request)
            .flatMap { service.createTask(it) }
            .map { ResponseEntity(it, HttpStatus.OK) }
            .onErrorResume { Mono.just(ResponseEntity.of(handleException(it)).build()) }

    @GetMapping(TaskRoute.GET_TASK)
    fun getTask(@PathVariable id: String): Mono<ResponseEntity<TaskResponse>> =
        service.getTaskResponse(id)
            .map { ResponseEntity(it, HttpStatus.OK) }
            .onErrorResume { Mono.just(ResponseEntity.of(handleException(it)).build()) }

    private fun validate(request: CreateTaskRequest): Mono<CreateTaskRequest> =
        run { validator.validate(request).joinToString(separator = "\n", transform = { it.message }) }
            .let {
                if (it.isNotEmpty()) { Mono.error(TaskRequestException(it)) }
                else { Mono.just(request) }
            }

    private fun handleException(cause: Throwable): ProblemDetail =
        when(cause) {
            is TaskNotFoundException -> translator.toProblemDetail(HttpStatus.NOT_FOUND, cause)
            is TaskException -> translator.toProblemDetail(HttpStatus.BAD_REQUEST, cause)
            else -> ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, cause.message)
        }
}