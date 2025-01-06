package com.ptitB22DCCN539.todoList.Redis.CodeVerify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeVerifyRedis implements Serializable {
    private String code;
    private String email;
    private Long timeToLive;
}
