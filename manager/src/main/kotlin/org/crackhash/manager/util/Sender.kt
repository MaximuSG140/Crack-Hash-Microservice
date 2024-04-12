package org.crackhash.manager.util

import reactor.core.publisher.Mono

interface Sender {

    operator fun <T : Any> invoke(requests: List<T>): Mono<Unit>
}