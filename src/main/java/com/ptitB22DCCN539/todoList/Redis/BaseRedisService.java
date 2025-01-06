package com.ptitB22DCCN539.todoList.Redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService<K, F, V> {
    void set(K key, V value);
    void setTimeToLive(K key, Long timeToLive);
    void hashSet(K key, F field, V value);
    boolean hasExists(K key, F field);
    V get(K key);
    Map<F, V> getField(K key);
    V hashGet(K key, F field);
    Set<F> getFieldPrefix(K key);
    void delete(K key);
    void delete(K key, F field);
    void deleteAll(List<K> keys);
    void delete(K key, List<F> fields);
    Set<K> getKeysByPrefix(K fieldPrefix);
}
