package org.crackhash.worker.util

interface Sender {

    operator fun <T : Any> invoke(requests: List<T>)
}