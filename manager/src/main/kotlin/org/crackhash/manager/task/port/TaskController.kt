package org.crackhash.manager.task.port

import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.dto.CreateTaskRequest
import org.crackhash.manager.task.api.dto.TaskResponse
import org.crackhash.manager.util.Route
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(Route.API)
class TaskController(private val service: TaskService) {

    @PostMapping(Route.CREATE_TASK)
    fun createTask(@RequestBody request: CreateTaskRequest): Mono<String> = service.createTask(request)

    @GetMapping(Route.GET_TASK)
    fun getTask(@PathVariable id: String): Mono<TaskResponse> = service.getTaskResponse(id)
}