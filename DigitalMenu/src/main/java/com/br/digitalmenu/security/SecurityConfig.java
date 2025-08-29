package com.br.digitalmenu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/adm/**").hasRole("FUNCIONARIO_ADM")
                        .requestMatchers("/garcom/**").hasRole("FUNCIONARIO_GARCOM")
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
