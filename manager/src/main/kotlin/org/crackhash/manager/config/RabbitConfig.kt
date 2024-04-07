package org.crackhash.manager.config

import com.fasterxml.jackson.databind.ObjectMapper
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
    fun createDeclarables(properties: ManagerConfigurationProperties): Declarables =
        Declarables(
            QueueBuilder.durable(properties.managerQueue)
                .deadLetterExchange(properties.managerQueue)
                .ttl(properties.ttl * 1000)
                .build(),
            QueueBuilder.durable(properties.workerQueue)
                .deadLetterExchange(properties.workerQueue)
                .ttl(properties.ttl * 1000)
                .build()
        )

    @Bean
    @ConditionalOnBean(RabbitConfig::class)
    fun createSender(properties: ManagerConfigurationProperties, template: RabbitTemplate, mapper: ObjectMapper): Sender =
        RabbitSender(template, properties.workerQueue, mapper)

    class RabbitSender(
        private val template: RabbitTemplate,
        private val queue: String,
        private val mapper: ObjectMapper
    ) : Sender {

        override fun <T : Any> invoke(requests: List<T>): Unit =
            requests.forEach { template.convertAndSend(queue, mapper.writeValueAsString(it)) }
    }
}