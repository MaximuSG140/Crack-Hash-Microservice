package org.crackhash.manager.task.config

import org.crackhash.manager.task.impl.WebSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun sender(properties: TaskConfigurationProperties): WebSender =
        WebSender(
            WebClient.create(properties.workerUrl)
                .method(HttpMethod.POST)
                .uri(TaskRoute.INTERNAL_API + TaskRoute.CREATE_SUBTASK)
        )
}