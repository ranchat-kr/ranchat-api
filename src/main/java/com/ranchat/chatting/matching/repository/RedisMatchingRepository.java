package com.ranchat.chatting.matching.repository;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RedisMatchingRepository implements MatchingRepository {
    private static final String WAITING_QUEUE_PREFIX = "WAITING_QUEUE::";
    private static final Duration QUEUE_EXPIRATION = Duration.ofSeconds(30);
    private final RedisTemplate<String, String> redisTemplate;

    public RedisMatchingRepository(RedisConnectionFactory redisConnectionFactory) {
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
    public List<String> findWaitingUsers() {
        return redisTemplate.keys(WAITING_QUEUE_PREFIX + "*")
            .stream()
            .map(key -> key.replace(WAITING_QUEUE_PREFIX, ""))
            .collect(Collectors.toList());
    }

    @Override
    public void addUser(String userId) {
        redisTemplate.opsForValue()
            .set(WAITING_QUEUE_PREFIX + userId, userId, QUEUE_EXPIRATION);
    }

    @Override
    public void deleteUsers(List<String> userId) {
        var keys = userId.stream()
            .map(id -> WAITING_QUEUE_PREFIX + id)
            .toList();

        redisTemplate.delete(keys);
    }

    @Override
    public void deleteUser(String userId) {
        var key = WAITING_QUEUE_PREFIX + userId;

        redisTemplate.delete(key);
    }
}
