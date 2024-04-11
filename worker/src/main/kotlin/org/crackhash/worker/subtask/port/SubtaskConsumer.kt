package org.crackhash.worker.subtask.port

import com.rabbitmq.client.Channel
import kotlinx.serialization.json.Json
import org.crackhash.worker.config.Route
import org.crackhash.worker.subtask.api.SubtaskService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class SubtaskConsumer(private val commands: SubtaskService) {

    @RabbitListener(queues = [Route.WORKER_QUEUE])
    fun consume(request: String, channel: Channel, @Header(AmqpHeaders.DELIVERY_TAG) tag: Long): Unit =
        runCatching { commands.run(Json.decodeFromString(request)) }
            .onSuccess { channel.basicAck(tag, false) }
            .getOrThrow()
}