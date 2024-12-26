package com.ptitB22DCCN539.todoList.Redis.User.Repository;

import com.ptitB22DCCN539.todoList.Redis.User.CodeVerifyRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVerifyCodeRedisRepository extends CrudRepository<CodeVerifyRedis, String> {
}
