package org.crackhash.manager.task.impl

import kotlinx.serialization.json.JsonElement
import org.crackhash.manager.task.api.Sender
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class WebSender(private val requestBodySpec: WebClient.RequestBodySpec) : Sender {

    override fun invoke(requests: List<JsonElement>) {
        List(requests.size) {
            requestBodySpec.bodyValue(requests[it])
                .retrieve()
                .bodyToMono(Unit::class.java)
        }.forEach{ it.block() }
    }
}