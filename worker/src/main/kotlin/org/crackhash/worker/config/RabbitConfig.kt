package org.crackhash.worker.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableRabbit
@Configuration
@ConditionalOnProperty(prefix = "crack-hash-worker", name = ["version"], havingValue = "2")
class RabbitConfig {

    @Bean
    @ConditionalOnBean(RabbitConfig::class)
    fun createSender(properties: WorkerConfigurationProperties, template: RabbitTemplate, mapper: ObjectMapper): Sender =
        RabbitSender(template, Route.MANAGER_QUEUE, mapper)

    class RabbitSender(
        private val template: RabbitTemplate,
        private val queue: String,
        private val mapper: ObjectMapper
    ) : Sender {

        override fun <T : Any> invoke(requests: List<T>): Unit =
            requests.forEach { template.convertAndSend(queue, mapper.writeValueAsString(it)) }
    }
}