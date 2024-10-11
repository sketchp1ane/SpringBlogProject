package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;






@Service
public class QueryService {

    @Autowired
    private QueryRepository repository;

    public List<QueryResultDTO> getResultsBySort(String sort) {
        List<QueryResult> results = repository.findBySort(sort);
        return results.stream().map(result -> new QueryResultDTO(
                result.getTitle(),
                result.getContent(),
                Base64.getEncoder().encodeToString(result.getImageData())
        )).collect(Collectors.toList());
    }
}