package com.elearning.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/chapters/**").permitAll()
                        .anyRequest().permitAll()//authenticated()
                )
                .csrf(csrf -> csrf.disable())  // ✅ Compliant with Spring Security 6.1+
                .httpBasic(httpBasic -> {}); // or .formLogin() if preferred

        return http.build();
    }
}