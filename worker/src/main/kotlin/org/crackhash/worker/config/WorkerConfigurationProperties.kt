package org.crackhash.worker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crack-hash-worker")
data class WorkerConfigurationProperties(
    val version: String,
    val manager: Manager,
    val queue: String
) {

    data class Manager(
        val host: String,
        val port: Int,
        val queue: String
    )
}