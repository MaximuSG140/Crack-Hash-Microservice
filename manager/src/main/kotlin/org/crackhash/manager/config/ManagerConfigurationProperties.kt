package org.crackhash.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crack-hash-manager")
data class ManagerConfigurationProperties(
    val version: String,
    val ttl: Int,
    val partCount: Int,
    val alphabet: String,
    val uri: String
)
