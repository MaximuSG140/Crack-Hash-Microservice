package org.crackhash.manager.task.port

import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.dto.CreateTaskRequest
import org.crackhash.manager.task.api.dto.TaskResponse
import org.crackhash.manager.task.config.TaskRoute
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(TaskRoute.API)
class TaskController(private val service: TaskService) {

    @PostMapping(TaskRoute.CREATE_TASK)
    fun createTask(@RequestBody request: CreateTaskRequest): Mono<String> = service.createTask(request)

    @GetMapping(TaskRoute.GET_TASK)
    fun getTask(@PathVariable id: String): Mono<TaskResponse> = service.getTaskResponse(id)
}