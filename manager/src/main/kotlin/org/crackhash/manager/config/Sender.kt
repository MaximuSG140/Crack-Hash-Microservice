package org.crackhash.manager.config

interface Sender {

    operator fun <T : Any> invoke(requests: List<T>)
}