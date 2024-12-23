package com.ptitB22DCCN539.todoList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;

@Repository
public interface IJwtTokenRepository extends JpaRepository<JwtTokenEntity, String> {

}
