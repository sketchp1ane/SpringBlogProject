package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @GetMapping("/query/{sort}")
    public List<QueryResultDTO> getResultsBySort(@PathVariable String sort) {
        return queryService.getResultsBySort(sort);
    }
}