package org.crackhash.manager.util

interface Sender {

    operator fun <T : Any> invoke(requests: List<T>)
}