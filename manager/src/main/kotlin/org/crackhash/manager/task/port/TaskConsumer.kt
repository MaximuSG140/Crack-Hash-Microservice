package org.crackhash.manager.task.port

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import org.crackhash.manager.config.Route
import org.crackhash.manager.task.api.TaskCommands
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class TaskConsumer(private val commands: TaskCommands, private val mapper: ObjectMapper) {

    @RabbitListener(queues = [Route.QUEUE])
    fun consume(str: String, channel: Channel, @Header(AmqpHeaders.DELIVERY_TAG) tag: Long): Unit =
        runCatching { mapToCompletedSubtaskEvent(str) }
            .mapCatching { commands.updateTask(it) }
            .onSuccess { channel.basicAck(tag, false) }
            .getOrThrow()

    private fun mapToCompletedSubtaskEvent(str: String): CompletedSubtaskEvent =
        mapper.readValue(str, CompletedSubtaskEvent::class.java)
}