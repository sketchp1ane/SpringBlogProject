package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageLogService {

    @Autowired
    private MessageLogRepository messageLogRepository;

    // 获取所有日志记录
    public List<MessageLog> getAllLogs() {
        return messageLogRepository.findAll();
    }

    // 获取分页的日志记录
    public Page<MessageLog> getLogsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageLogRepository.findAll(pageable);
    }

    // 根据操作类型查询日志
    public List<MessageLog> getLogsByOperationType(String operationType) {
        return messageLogRepository.findByOperationType(operationType);
    }

    // 根据时间范围查询日志
    public List<MessageLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return messageLogRepository.findByChangeTimeBetween(start, end);
    }

    // 删除日志和对应的消息，使用事务管理
    @Transactional
    public void deleteLogsAndMessages(List<Long> logIds, List<Long> messageIds) {
        messageLogRepository.deleteAllById(logIds);
        // 假设还有 messageRepository 处理消息的删除
        // messageRepository.deleteAllById(messageIds);
    }
}
