package org.crackhash.manager.config

import org.crackhash.manager.util.Sender
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Configuration
class WebClientConfig {

    @Bean
    @ConditionalOnMissingBean(RabbitConfig::class)
    fun createSender(properties: ManagerConfigurationProperties): WebSender =
        WebSender(WebClient.create(properties.uri))

    class WebSender(private val webClient: WebClient) : Sender {

        override fun <T : Any> invoke(requests: List<T>) {
            Flux.fromIterable(List(requests.size) {
                webClient.method(HttpMethod.POST)
                    .uri(Route.INTERNAL_API + Route.CREATE_SUBTASK)
                    .bodyValue(requests[it])
                    .retrieve()
                    .bodyToMono(Unit::class.java)
            }).flatMap { it }.collectList().block()
        }
    }
}