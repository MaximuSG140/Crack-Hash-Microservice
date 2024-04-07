package org.crackhash.manager.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
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

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://mongo1:27017,mongo2:27017,mongo3:27017/test")
        val setting = MongoClientSettings.builder().applyConnectionString(connectionString).build()
        return MongoClients.create(setting)
    }
}