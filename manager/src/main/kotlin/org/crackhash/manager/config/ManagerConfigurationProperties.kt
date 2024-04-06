package org.crackhash.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crack-hash-manager")
data class ManagerConfigurationProperties(
    val version: String,
    val ttl: Long,
    val partCount: Int,
    val alphabet: String,
    val queue: String,
    val worker: Worker
) {

    data class Worker(
        val host: String,
        val port: Int,
        val ttl: Int,
        val queue: String
    )
}
