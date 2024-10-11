package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        // 获取当前登录用户的 email
        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail != null) {
            message.setEmail(currentUserEmail); // 将用户的 email 设置到留言中
            message.setCreateTime(LocalDateTime.now()); // 设置当前的 LocalDateTime
            Message savedMessage = messageRepository.save(message);
            return ResponseEntity.ok(savedMessage);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 未登录时返回 401
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMessage(@PathVariable Integer id, @RequestBody Message newMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 获取当前用户的角色
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message existingMessage = optionalMessage.get();

            // 检查当前用户是否是留言的所有者或者是管理员
            if (existingMessage.getEmail().equals(currentUserEmail) || isAdmin) {
                existingMessage.setContent(newMessage.getContent());
                messageRepository.save(existingMessage);
                return ResponseEntity.ok("留言已修改");
            } else {
                return ResponseEntity.status(403).body("无权限修改该留言");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // 删除留言
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 获取当前用户的角色
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();

            // 检查当前用户是否是留言的所有者或者是管理员
            if (message.getEmail().equals(currentUserEmail) || isAdmin) {
                messageRepository.delete(message);
                return ResponseEntity.ok("留言已删除");
            } else {
                return ResponseEntity.status(403).body("无权限删除该留言");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // 获取当前用户的 email（或 username）
        } else {
            return null; // 未登录
        }
    }
}
