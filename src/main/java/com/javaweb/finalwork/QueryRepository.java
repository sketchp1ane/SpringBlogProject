package com.javaweb.finalwork;

import org.springframework.data.repository.CrudRepository;


import java.util.List;


public interface QueryRepository extends CrudRepository<QueryResult, Integer> {
    List<QueryResult> findBySort(String sort);


}
