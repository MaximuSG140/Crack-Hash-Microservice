package org.crackhash.worker.subtask.api

import kotlinx.serialization.json.JsonElement

interface Sender {
    operator fun invoke(request: JsonElement)
}