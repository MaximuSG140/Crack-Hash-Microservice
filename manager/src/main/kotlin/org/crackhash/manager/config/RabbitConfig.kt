package org.crackhash.manager.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.crackhash.manager.util.Sender
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableRabbit
@Configuration
@ConditionalOnProperty(prefix = "crack-hash-manager", name = ["version"], havingValue = "2")
class RabbitConfig {

    @Bean
    @ConditionalOnBean(RabbitConfig::class)
    fun createDeclarables(properties: ManagerConfigurationProperties): Declarables =
        Declarables(
            QueueBuilder.durable(Route.MANAGER_QUEUE)
                .deadLetterExchange(Route.MANAGER_QUEUE)
                .ttl(properties.ttl * 1000)
                .build(),
            QueueBuilder.durable(Route.WORKER_QUEUE)
                .deadLetterExchange(Route.WORKER_QUEUE)
                .ttl(properties.ttl * 1000)
                .build()
        )

    @Bean
    @ConditionalOnBean(RabbitConfig::class)
    fun createSender(properties: ManagerConfigurationProperties, template: RabbitTemplate, mapper: ObjectMapper): Sender =
        RabbitSender(template, Route.WORKER_QUEUE, mapper)

    class RabbitSender(
        private val template: RabbitTemplate,
        private val queue: String,
        private val mapper: ObjectMapper
    ) : Sender {

        override fun <T : Any> invoke(requests: List<T>): Unit =
            requests.forEach { template.convertAndSend(queue, mapper.writeValueAsString(it)) }
    }
}