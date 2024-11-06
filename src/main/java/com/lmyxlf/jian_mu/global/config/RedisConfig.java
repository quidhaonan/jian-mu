package com.lmyxlf.jian_mu.global.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lmyxlf.jian_mu.global.constant.SysConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/29 0:49
 * @description
 * @since 17
 */

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.password}")
    private String redisPassword;

    private static final Integer TIMEOUT = 3000;
    private static final Integer THREADS = 4;
    private static final Integer DATABASE = 0;
    private static final Integer NETTY_THREADS = 4;
    private static final Integer RETRY_ATTEMPTS = 3;
    private static final Integer RETRY_INTERVAL = 1500;
    private static final Integer CONNECT_TIMEOUT = 10000;
    private static final Integer CONNECTION_POOL_SIZE = 16;
    private static final Integer DNS_MONITORING_INTERVAL = 5000;
    private static final Integer IDLE_CONNECTION_TIMEOUT = 10000;
    private static final Integer SUBSCRIPTIONS_PER_CONNECTION = 5;
    private static final Integer CONNECTION_MINIMUM_IDLE_SIZE = 8;
    private static final Integer SUBSCRIPTION_CONNECTION_POOL_SIZE = 8;
    private static final Integer SUBSCRIPTION_CONNECTION_MINIMUM_IDLE_SIZE = 1;

    /**
     * 注入redisson 客户端
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown", name = "redissonClient")
    public RedissonClient useSingleServer() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisHost)
                .setPassword(redisPassword)
                .setTimeout(TIMEOUT)
                .setDatabase(DATABASE)
                .setClientName(SysConstant.LMYXLF)
                .setRetryAttempts(RETRY_ATTEMPTS)
                .setRetryInterval(RETRY_INTERVAL)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionPoolSize(CONNECTION_POOL_SIZE)
                .setDnsMonitoringInterval(DNS_MONITORING_INTERVAL)
                .setIdleConnectionTimeout(IDLE_CONNECTION_TIMEOUT)
                .setSubscriptionsPerConnection(SUBSCRIPTIONS_PER_CONNECTION)
                .setConnectionMinimumIdleSize(CONNECTION_MINIMUM_IDLE_SIZE)
                .setSubscriptionConnectionPoolSize(SUBSCRIPTION_CONNECTION_POOL_SIZE)
                .setSubscriptionConnectionMinimumIdleSize(SUBSCRIPTION_CONNECTION_MINIMUM_IDLE_SIZE);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决 jackson2 无法反序列化 LocalDateTime
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());

        config
                .setThreads(THREADS)
                .setNettyThreads(NETTY_THREADS)
                .setCodec(new JsonJacksonCodec(om))
                .setTransportMode(TransportMode.NIO);

        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        // 解决 LocalDateTime 序列化与反序列化不一致
        Jackson2JsonRedisSerializer<Object> j2jrs = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决 jackson2 无法反序列化 LocalDateTime
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        j2jrs.setObjectMapper(om);
        // 序列化 value 时使用此序列化方法
        redisTemplate.setValueSerializer(j2jrs);
        redisTemplate.setHashValueSerializer(j2jrs);

        return redisTemplate;
    }
}