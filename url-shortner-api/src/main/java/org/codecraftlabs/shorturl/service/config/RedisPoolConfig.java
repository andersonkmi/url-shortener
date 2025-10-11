package org.codecraftlabs.shorturl.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nonnull;

@Configuration
public class RedisPoolConfig {

    private final RedisConfigurationProperties redisConfigurationProperties;

    @Autowired
    public RedisPoolConfig(@Nonnull RedisConfigurationProperties redisConfigurationProperties) {
        this.redisConfigurationProperties = redisConfigurationProperties;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisConfigurationProperties.getHost());
        config.setPort(redisConfigurationProperties.getPort());
        config.setUsername(redisConfigurationProperties.getUserName());
        config.setPassword(redisConfigurationProperties.getPassword());

        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(jedisPoolConfig())
                .build();

        return new JedisConnectionFactory(config, clientConfig);
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfigurationProperties.getMaxTotal());
        poolConfig.setMaxIdle(redisConfigurationProperties.getMaxIdle());
        poolConfig.setMinIdle(redisConfigurationProperties.getMinIdle());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        return poolConfig;
    }
}