package org.crackhash.worker.config

import org.crackhash.worker.util.Sender
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@EnableAsync
@Configuration
class WebClientConfig {

    @Bean
    @ConditionalOnMissingBean(RabbitConfig::class)
    fun createWebSender(properties: WorkerConfigurationProperties): WebSender =
        WebSender(WebClient.create(properties.uri))

    class WebSender(private val webClient: WebClient) : Sender {

        override fun <T : Any> invoke(requests: List<T>) {
            Flux.fromIterable(
                List(requests.size) {
                    webClient.method(HttpMethod.PATCH)
                        .uri(Route.INTERNAL_API + Route.UPDATE_TASK)
                        .bodyValue(requests[it])
                        .retrieve()
                        .bodyToMono(Unit::class.java)
                }
            ).flatMap { it }.collectList().block()
        }
    }
}