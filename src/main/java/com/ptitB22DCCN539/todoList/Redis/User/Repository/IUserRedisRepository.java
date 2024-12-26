package com.ptitB22DCCN539.todoList.Redis.User.Repository;

import com.ptitB22DCCN539.todoList.Redis.User.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRedisRepository extends CrudRepository<UserRedis, String>, PagingAndSortingRepository<UserRedis, String> {
}
