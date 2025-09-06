package com.personal.mall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@EnableConfigurationProperties(CacheProperties.class)
@Configuration
public class SpringCacheConfig {

    //    配置缓存序列化，将缓存数据保存为json数据，默认缓存的数据使用的是jdk序列化机制
    @Bean
    public RedisCacheConfiguration getRedisCacheConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        // 配置以下内容，否则在配置文件中设置的数据不生效
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive()); // 过期时间
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix()); // key的前缀
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues(); // 缓存为null保存，防止缓存穿透
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix(); // 是否使用key的前缀
        }

        return config;
    }
}
