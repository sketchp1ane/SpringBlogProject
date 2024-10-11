package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;  // 引入密码加密
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // 注入密码加密器

    // 注册用户
    public User register(User user) {
        // 检查用户是否已经存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 如果用户没有提供角色，默认设置为 "USER"
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // 直接保存用户，不进行密码加密
        return userRepository.save(user);
    }

    // 用户登录认证
    public User authenticate(String email, String password) {
        // 查找用户是否存在
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 使用 passwordEncoder.matches() 来验证加密后的密码
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user; // 返回用户对象，表示登录成功
            } else {
                throw new IllegalArgumentException("Invalid password"); // 密码错误
            }
        } else {
            throw new IllegalArgumentException("User not found"); // 用户不存在
        }
    }

    // 获取用户信息
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        Iterable<User> iterableUsers = userRepository.findAll(); // Returns Iterable<User>

        // Convert Iterable to List
        List<User> userList = new ArrayList<>();
        iterableUsers.forEach(userList::add);

        return userList;
    }


    // 更新用户信息
    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword())); // 重新设置密码（加密）
            return userRepository.save(userToUpdate);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 删除用户
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
