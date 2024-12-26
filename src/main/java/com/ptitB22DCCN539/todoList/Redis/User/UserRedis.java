package com.ptitB22DCCN539.todoList.Redis.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash(value = "userRedis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRedis implements Serializable {
    @Id
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private List<RoleRedis> roles;
}
