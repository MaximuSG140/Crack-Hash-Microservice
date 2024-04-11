package org.crackhash.manager.task.port

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import org.crackhash.manager.config.RabbitConfig
import org.crackhash.manager.task.api.TaskService
import org.crackhash.manager.task.api.event.CompletedSubtaskEvent
import org.crackhash.manager.util.Route
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(RabbitConfig::class)
class TaskConsumer(private val service: TaskService, private val mapper: ObjectMapper) {

    @RabbitListener(queues = [Route.MANAGER_QUEUE])
    fun consume(str: String, channel: Channel, @Header(AmqpHeaders.DELIVERY_TAG) tag: Long): Unit =
        service.updateTask(mapToCompletedSubtaskEvent(str))
            .doOnSuccess { channel.basicAck(tag, false) }
            .block() ?: throw IllegalStateException()

    private fun mapToCompletedSubtaskEvent(str: String): CompletedSubtaskEvent =
        mapper.readValue(str, CompletedSubtaskEvent::class.java)
}