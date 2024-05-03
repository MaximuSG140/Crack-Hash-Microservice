package org.crackhash.manager.task.api

import kotlinx.serialization.json.JsonElement
import reactor.core.publisher.Mono

interface Sender {
    operator fun invoke(requests: List<JsonElement>)
}