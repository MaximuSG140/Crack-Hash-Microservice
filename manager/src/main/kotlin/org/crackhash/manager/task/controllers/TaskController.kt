package org.crackhash.manager.task.controllers

import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.contracts.CreateTaskRequest
import org.crackhash.manager.task.api.contracts.TaskResponse
import org.crackhash.manager.task.config.TaskRoute
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(TaskRoute.API)
class TaskController(
    private val service: TaskService
) {

    @PostMapping(TaskRoute.CREATE_TASK)
    fun createTask(@RequestBody request: CreateTaskRequest): String = service.createTask(request)

    @GetMapping(TaskRoute.GET_TASK)
    fun getTask(@RequestParam requestId: String): TaskResponse = service.getTaskResponse(requestId)
}