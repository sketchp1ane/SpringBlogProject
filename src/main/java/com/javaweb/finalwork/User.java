package com.javaweb.finalwork;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // 这个注解告诉 Hibernate 将此类映射为数据库表
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String email;    // 用户的邮箱，将作为登录凭据

    private String password; // 用户的密码，存储为明文（简化项目）

    private String role = "USER"; // 用户的角色，默认是 "USER"

    // 带 email, password 和 role 的构造函数
    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // 无参构造函数，默认角色是 "USER"
    public User() {
        this.role = "USER"; // 默认角色为普通用户
    }

    // Getters 和 Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
