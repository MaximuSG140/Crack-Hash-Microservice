package org.crackhash.worker.subtask.port

import org.crackhash.worker.config.Route
import org.crackhash.worker.subtask.api.event.CreatedTaskEvent
import org.crackhash.worker.subtask.api.SubtaskCommands
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Route.INTERNAL_API)
class InternalSubtaskController(private val commands: SubtaskCommands) {

    @PostMapping(Route.CREATE_SUBTASK)
    fun runSubtask(@RequestBody event: CreatedTaskEvent): Unit = commands.runAsync(event)

}