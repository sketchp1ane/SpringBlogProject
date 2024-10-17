package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class MessageLogController {

    @Autowired
    private MessageLogService messageLogService;

    // 获取所有日志记录
    @GetMapping
    public List<MessageLog> getAllLogs() {
        return messageLogService.getAllLogs();
    }

    // 获取分页的日志记录
    @GetMapping("/paged")
    public Page<MessageLog> getLogsPaged(
            @RequestParam int page,
            @RequestParam int size) {
        return messageLogService.getLogsPaged(page, size);
    }

    // 根据操作类型查询日志
    @GetMapping("/operation/{operationType}")
    public List<MessageLog> getLogsByOperationType(@PathVariable String operationType) {
        return messageLogService.getLogsByOperationType(operationType);
    }

    // 根据时间范围查询日志
    @GetMapping("/date")
    public List<MessageLog> getLogsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return messageLogService.getLogsByDateRange(start, end);
    }
}
