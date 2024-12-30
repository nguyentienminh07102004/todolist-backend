package com.ptitB22DCCN539.todoList.Redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService {
    void set(String key, String value);
    void setTimeToLive(String key, Long timeToLive);
    void hashSet(String key, String field, Object value);
    boolean hasExists(String key, String field);
    Object get(String key);
    Map<String, Object> getField(String key);
    Object hashGet(String key, String field);
    List<Object> hashGetByFieldPrefix(String key, String fieldPrefix);
    Set<String> getFieldPrefix(String key);
    void delete(String key);
    void delete(String key, String field);
    void delete(String key, List<String> fields);
}