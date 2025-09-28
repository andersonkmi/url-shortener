package org.codecraftlabs.shorturl.service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
public class URLShortnerCachingRepository {
    private static final String OK_RESPONSE = "OK";
    private static final Logger logger = LoggerFactory.getLogger(URLShortnerCachingRepository.class);
    private final JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    public URLShortnerCachingRepository(@Nonnull JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    public void setValue(@Nonnull String key, @Nonnull String value) {
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            String response = jedis.set(key, value);
            if (OK_RESPONSE.equals(response)) {
                logger.info("Pair '{}' and '{}' saved in cache", key, value);
            } else {
                logger.warn("Pair '{}' and '{}' NOT saved in cache", key, value);
            }
        } catch (RedisConnectionFailureException exception) {
            logger.warn("Pair '{}' and '{}' NOT saved in cache", key, value, exception);
        }
    }

    @Nonnull
    public Optional<String> getValue(String key) {
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            return Optional.ofNullable(jedis.get(key));
        } catch (RedisConnectionFailureException exception) {
            logger.warn("Key '{}' NOT found in cache", key, exception);
            return Optional.empty();
        }
    }
}
