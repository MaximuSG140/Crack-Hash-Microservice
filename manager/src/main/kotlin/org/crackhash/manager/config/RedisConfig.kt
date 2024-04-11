package org.crackhash.manager.config

import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {

//    @Bean
//    fun createReactiveRedisTemplate(mapper: ObjectMapper, factory: ReactiveRedisConnectionFactory): ReactiveStringRedisTemplate =
//        run { StringRedisSerializer() }
//            .let {
//                ReactiveRedisTemplate(
//                    factory,
//                    RedisSerializationContext.newSerializationContext<String, String>()
//                        .key(it)
//                        .value(it)
//                        .hashKey(it)
//                        .hashValue(it)
//                        .build()
//                )
//            }
}