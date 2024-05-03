package org.crackhash.worker.subtask.impl

import kotlinx.serialization.json.JsonElement
import org.crackhash.worker.subtask.api.Sender
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

class WebSender(private val requestBodySpec: RestClient.RequestBodySpec) : Sender {
    override fun invoke(request: JsonElement) {
        requestBodySpec.body(request).retrieve()
    }
}