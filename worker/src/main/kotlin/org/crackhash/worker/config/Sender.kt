package org.crackhash.worker.config

interface Sender {

    operator fun <T : Any> invoke(requests: List<T>)
}