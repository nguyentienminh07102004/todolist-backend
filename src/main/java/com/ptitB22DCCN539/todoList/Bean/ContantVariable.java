package com.ptitB22DCCN539.todoList.Bean;

import org.springframework.security.core.context.SecurityContextHolder;

public class ContantVariable {
    public static String TOKEN_NAME = "token";
    public static String CODE_VERIFIED = "codeVerify";
    public static String ROLE_USER = "USER";
    public static String ROLE_PREFIX = "ROLE_";

    // redis
    public static String CATEGORY_REDIS_PREFIX = "category";
    public static String getCategoryRedisPrefix() {
        return "%s:%s".formatted(CATEGORY_REDIS_PREFIX, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static String TASK_REDIS_PREFIX = "task";
    public static String getTaskRedisPrefix() {
        return "%s:%s".formatted(TASK_REDIS_PREFIX, SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public static final Long EXPIRE_TASK_AFTER_DELETE = 2592000L;
}
