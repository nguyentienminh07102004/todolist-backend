package com.ptitB22DCCN539.todoList.Redis;

import java.util.Map;

public interface BaseRedisService {
    void set(String key, String value);
    void setTimeToLive(String key, Long timeToLive);
    void hashSet(String key, String field, Object value);
    boolean hasExists(String key, String field);
    Object get(String key);
    Map<String, Object> getField(String key);

}
