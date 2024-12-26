package com.ptitB22DCCN539.todoList.Redis.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "roleRedis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRedis implements Serializable {
    @Id
    private String code;
    private String name;
    private String description;
}