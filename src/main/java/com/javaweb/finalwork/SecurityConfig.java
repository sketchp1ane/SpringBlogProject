package com.javaweb.finalwork;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll() // 登录和注册不需要认证
                        .requestMatchers("/api/messages/**").authenticated() // 留言相关接口需要认证
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")   // 只有管理员能访问的接口
                        .anyRequest().authenticated() // 其他请求需要认证
                )
                .formLogin(form -> form
                        .loginPage("/login.html") // 自定义登录页面
                        .loginProcessingUrl("/api/user/login")
                        .defaultSuccessUrl("/index.html", true) // 登录成功后跳转页面
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        //.logoutSuccessUrl("/login.html") // 登出后跳转页面
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // 在API的场景下，禁用CSRF保护
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 基于Session的认证
                );

        return http.build();
    }

    // 使用 NoOpPasswordEncoder 实现明文密码的存储
    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder 是一个明文存储密码的编码器
        return NoOpPasswordEncoder.getInstance();
    }

    // 用于自定义获取用户信息的 UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    // 自定义 AuthenticationManager 的 Bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); // 使用明文密码进行匹配
        return auth.build();
    }
}
