package com.ranchat.chatting.matching.repository;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class MatchingRepositoryImpl implements MatchingRepository {
    private static final String MATCHING_KEY = "waiting-users";
    private final RedisTemplate<String, String> redisTemplate;
    private final SetOperations<String, String> setOps;

    public MatchingRepositoryImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisTemplate = initRedisTemplate(redisConnectionFactory);
        this.setOps = redisTemplate.opsForSet();
    }

    private RedisTemplate<String, String> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<String, String>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(String.class));
        return template;
    }

    @Override
    public void addParticipant(String participantId) {
        setOps.add(MATCHING_KEY, participantId);
    }

    @Override
    public void deleteParticipant(String participantId) {
        setOps.remove(MATCHING_KEY, participantId);
    }
}
