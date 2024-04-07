package org.crackhash.worker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crack-hash-worker")
data class WorkerConfigurationProperties(
    val version: String,
    val uri: String,
    val queue: String
)