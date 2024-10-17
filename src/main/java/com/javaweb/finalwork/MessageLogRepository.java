package com.javaweb.finalwork;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {

    // 根据操作类型查询日志
    List<MessageLog> findByOperationType(String operationType);

    // 根据时间范围查询日志
    List<MessageLog> findByChangeTimeBetween(LocalDateTime start, LocalDateTime end);
}
