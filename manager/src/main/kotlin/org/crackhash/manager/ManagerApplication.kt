package org.crackhash.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableDiscoveryClient
class ManagerApplication

fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}
