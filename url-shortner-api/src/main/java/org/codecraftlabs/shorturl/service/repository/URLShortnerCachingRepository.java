package org.codecraftlabs.shorturl.service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;

@Component
public class URLShortnerCachingRepository {
    private static final Logger logger = LoggerFactory.getLogger(URLShortnerCachingRepository.class);
    private final JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    public URLShortnerCachingRepository(@Nonnull JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    public void setValue(@Nonnull String key, @Nonnull String value) {
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            jedis.set(key, value);
            logger.info("Pair '{}' and '{}' saved in cache", key, value);
        }
    }

    public String getValue(String key) {
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            return jedis.get(key);
        }
    }
}
