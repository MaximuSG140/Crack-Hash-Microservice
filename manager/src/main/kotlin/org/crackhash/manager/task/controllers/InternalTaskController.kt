package org.crackhash.manager.task.controllers

import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.contracts.CompletedSubtaskEvent
import org.crackhash.manager.task.config.TaskRoute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(TaskRoute.INTERNAL_API + TaskRoute.UPDATE_TASK)
class InternalTaskController(private val service: TaskService) {
    @PatchMapping
    fun updateTask(@RequestBody event: CompletedSubtaskEvent): Unit = service.updateTask(event)
}