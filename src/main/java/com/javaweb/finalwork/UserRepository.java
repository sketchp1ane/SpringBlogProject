package com.javaweb.finalwork;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    // 使用 Optional 来避免用户不存在时抛出 NullPointerException
    Optional<User> findByEmail(String email);



}
