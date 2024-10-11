package com.javaweb.finalwork;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findAllByOrderByCreateTimeDesc();
    Optional<Message> findById(Integer id);

}
