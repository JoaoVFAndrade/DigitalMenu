package com.br.digitalmenu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF
                .csrf(csrf -> csrf.disable())

                // Configura CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configura autorização das requisições
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                "/auth/**",
                                "/clientes/cadastro",
                                "/clientes/confirmar",
                                "/clientes/reenviar-codigo",
                                "/recuperacao/**"
                        ).permitAll()
                        // acessos do adm ->
                        .requestMatchers("/adm/**",
                                "/categorias/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/produtos/**",
                                "/funcionarios/**",
                                "/diaSemana/**"
                        ).hasAuthority("FUNCIONARIO_ADM")

                        .requestMatchers("/garcom/**").hasAuthority("FUNCIONARIO_GARCOM") //TODO ajustar os endpoitns que o garcom pode acessar
                        // Acessos do cliente ->
                        .requestMatchers(HttpMethod.GET,
                                "/clientes/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/produtos/**",
                                "/funcionarios/**",
                                "/diaSemana/**",
                                "/categorias/**"
                        ).hasAnyAuthority("CLIENTE", "FUNCIONARIO_ADM")
                        .anyRequest().authenticated()
                );

        // Adiciona o filtro JWT
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
