package org.crackhash.worker.subtask.port

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import org.crackhash.worker.config.Route
import org.crackhash.worker.subtask.api.event.CreatedTaskEvent
import org.crackhash.worker.subtask.api.SubtaskCommands
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class SubtaskConsumer(private val commands: SubtaskCommands, private val mapper: ObjectMapper) {

    @RabbitListener(queues = [Route.WORKER_QUEUE])
    fun consume(str: String, channel: Channel, @Header(AmqpHeaders.DELIVERY_TAG) tag: Long): Unit =
        runCatching { commands.run(mapToCreatedTaskEvent(str)) }
            .onSuccess { channel.basicAck(tag, false) }
            .getOrThrow()

    private fun mapToCreatedTaskEvent(str: String): CreatedTaskEvent =
        mapper.readValue(str, CreatedTaskEvent::class.java)
}