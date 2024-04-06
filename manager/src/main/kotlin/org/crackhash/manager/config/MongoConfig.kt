package org.crackhash.manager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories("org.crackhash.manager.task.impl.data.dao")
class MongoConfig: AbstractMongoClientConfiguration() {

    @Bean
    fun createTransactionalManager(factory: MongoDatabaseFactory): MongoTransactionManager =
        run { MongoTransactionManager(factory) }

    override fun getDatabaseName(): String = "test"
}