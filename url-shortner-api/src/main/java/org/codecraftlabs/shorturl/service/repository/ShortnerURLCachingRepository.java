package org.codecraftlabs.shorturl.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;

@Component
public class ShortnerURLCachingRepository {
    private final JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    public ShortnerURLCachingRepository(@Nonnull JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    public void setValue(@Nonnull String key, @Nonnull String value) {
        Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection();
        jedis.set(key, value);
        jedis.close();
    }
}
