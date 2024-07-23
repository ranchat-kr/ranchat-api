package com.ranchat.chatting.common.component;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Repository
public class RedisSessionRepository implements WebSocketSessionRepository {
    private static final String SESSION_KEY_PREFIX = "WEBSOCKET_SESSION::";
    private final RedisTemplate<String, String> redisTemplate;

    public RedisSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        this.redisTemplate = initRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate<String, String> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<String, String>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Override
    public void save(String userId, String sessionId) {
        redisTemplate.opsForValue()
            .set(SESSION_KEY_PREFIX + userId, sessionId);
    }

    @Override
    public Optional<String> findSessionId(String userId) {
        return Optional.ofNullable(
            redisTemplate.opsForValue()
                .get(SESSION_KEY_PREFIX + userId)
        );
    }

    @Override
    public void deleteByUserId(String userId) {
        redisTemplate.delete(SESSION_KEY_PREFIX + userId);
    }

    @Override
    public void deleteBySessionId(String sessionId) {

    }
}
