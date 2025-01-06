package com.ptitB22DCCN539.todoList.Redis.CodeVerify;

import java.util.Optional;

public interface ICodeVerifyService {
    CodeVerifyRedis save(CodeVerifyRedis codeVerifyRedis);
    Optional<CodeVerifyRedis> findByCode(String code);
}
