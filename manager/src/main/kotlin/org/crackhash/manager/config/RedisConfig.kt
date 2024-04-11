package org.crackhash.manager.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.crackhash.manager.task.impl.Task
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun createReactiveRedisTemplate(mapper: ObjectMapper, factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Task> =
        run { Jackson2JsonRedisSerializer(mapper, Task::class.java) }
            .let {
                ReactiveRedisTemplate(
                    factory,
                    RedisSerializationContext.newSerializationContext<String, Task>()
                        .key(StringRedisSerializer())
                        .value(it)
                        .hashKey(StringRedisSerializer())
                        .hashValue(it)
                        .build()
                )
            }
}