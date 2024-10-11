package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册用户
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 确保传递的用户对象有角色信息，如果没有提供角色，则设置为默认角色 "USER"
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }

            User newUser = userService.register(user);  // 调用服务注册用户
            response.put("success", true);
            response.put("user", newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 返回201 Created
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 返回400 Bad Request
        }
    }

    // 登录用户
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User authenticatedUser = userService.authenticate(user.getEmail(), user.getPassword());  // 调用服务验证用户
            response.put("success", true);
            response.put("user", authenticatedUser);  // 返回用户信息
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 返回401 Unauthorized
        }
    }

    // 获取用户信息
    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.findByEmail(email);  // 使用UserService获取用户信息
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 更新用户信息
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userService.findByEmail(user.getEmail());  // 查找用户
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setPassword(user.getPassword());  // 明文更新密码（建议后期加密）
            userService.updateUser(updatedUser);  // 保存更新后的用户信息
            response.put("success", true);
            response.put("user", updatedUser);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 返回404 Not Found
        }
    }

    // 删除用户
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userService.findByEmail(email);  // 查找用户
        if (existingUser.isPresent()) {
            userService.deleteUser(existingUser.get());  // 删除用户
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 返回404 Not Found
        }
    }

    @GetMapping("/login/status")
    public boolean checkLoginStatus() {
        // 获取当前认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 如果用户已认证，且不是匿名用户，则返回 true
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    @GetMapping("/email")
    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 如果用户已登录，返回用户的邮箱（用户名）
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName(); // 返回用户名（即邮箱）
        }

        return null; // 如果未登录，返回 null
    }

    @GetMapping("/role")
    public ResponseEntity<String> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities() != null) {
            // 获取用户唯一角色
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst() // 假设每个用户只有一个角色
                    .orElse("ROLE_USER"); // 默认角色为 ROLE_USER

            return ResponseEntity.ok(role); // 返回角色为纯文本
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
