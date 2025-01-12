package com.ptitB22DCCN539.todoList.Redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BaseRedisServiceImpl<K, F, V> implements BaseRedisService<K, F, V> {
    private final RedisTemplate<K, V> redisTemplate;
    private final HashOperations<K, F, V> hashOperations;

    public BaseRedisServiceImpl(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, Long timeToLive) {
        redisTemplate.expire(key, Duration.ofSeconds(timeToLive));
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hasExists(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<F, V> getField(K key) {
        return hashOperations.entries(key);
    }

    @Override
    public V hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public Set<F> getFieldPrefix(K key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(K key, F field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void deleteAll(List<K> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public void delete(K key, List<F> fields) {
        hashOperations.delete(key, fields);
    }

    @Override
    public Set<K> getKeysByPrefix(K fieldPrefix) {
        return redisTemplate.keys(fieldPrefix);
    }

    @Override
    public Long getTimeToLive(K key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
