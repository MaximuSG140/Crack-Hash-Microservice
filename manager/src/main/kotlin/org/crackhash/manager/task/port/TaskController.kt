package org.crackhash.manager.task.port

import jakarta.validation.Valid
import org.crackhash.manager.config.Route
import org.crackhash.manager.task.api.TaskCommands
import org.crackhash.manager.task.api.request.CreateTaskRequest
import org.crackhash.manager.task.api.response.TaskResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Route.API)
class TaskController(private val commands: TaskCommands) {

    @PostMapping(Route.CREATE_TASK)
    fun createTask(@Valid @RequestBody request: CreateTaskRequest): String = commands.createTask(request)

    @GetMapping(Route.GET_TASK)
    fun getTask(@PathVariable id: String): TaskResponse = commands.getTaskResponse(id)
}