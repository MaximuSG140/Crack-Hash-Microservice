package org.crackhash.manager.task.port

import org.crackhash.manager.config.Route
import org.crackhash.manager.task.api.TaskCommands
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Route.INTERNAL_API)
class InternalTaskController(private val commands: TaskCommands) {

    @PatchMapping(Route.UPDATE_TASK)
    fun updateTask(@RequestBody event: CompletedSubtaskEvent): Unit = commands.updateTask(event)
}