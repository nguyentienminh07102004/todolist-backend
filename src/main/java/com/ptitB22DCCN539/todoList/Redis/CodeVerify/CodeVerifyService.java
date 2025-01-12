package com.ptitB22DCCN539.todoList.Redis.CodeVerify;

import com.ptitB22DCCN539.todoList.Redis.BaseRedisServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class CodeVerifyService extends BaseRedisServiceImpl<String, String, Object> implements ICodeVerifyService {
    public CodeVerifyService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    @Transactional
    public CodeVerifyRedis save(CodeVerifyRedis codeVerifyRedis) {
        this.hashSet(String.format("codeVerify:%s", codeVerifyRedis.getCode()), "code", codeVerifyRedis.getCode());
        this.hashSet(String.format("codeVerify:%s", codeVerifyRedis.getCode()), "email", codeVerifyRedis.getEmail());
        this.setTimeToLive(String.format("codeVerify:%s", codeVerifyRedis.getCode()), codeVerifyRedis.getTimeToLive());
        return codeVerifyRedis;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CodeVerifyRedis> findByCode(String code) {
        Map<String, Object> properties = this.getField(String.format("codeVerify:%s", code));
        if (properties == null || properties.isEmpty()) {
            return Optional.empty();
        }
        CodeVerifyRedis codeVerifyRedis = CodeVerifyRedis.builder()
                .code(properties.get("code").toString())
                .email(properties.get("email").toString())
                .build();
        return Optional.of(codeVerifyRedis);
    }
}
