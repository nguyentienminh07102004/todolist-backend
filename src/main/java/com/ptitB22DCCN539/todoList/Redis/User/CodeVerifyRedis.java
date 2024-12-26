package com.ptitB22DCCN539.todoList.Redis.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash(value = "verifyCode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CodeVerifyRedis implements Serializable {
    @Id
    private String id;
    private String code;
    private String email;
    @TimeToLive
    private Long timeToLive;
}
