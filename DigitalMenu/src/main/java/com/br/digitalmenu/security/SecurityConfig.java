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
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth

                        //Rotas públicas (sem login)
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/clientes/cadastro",
                                "/clientes/confirmar",
                                "/clientes/reenviar-codigo",
                                "/recuperacao/**",
                                "/produtoPedido/**",
                                "/mesa/**"
                        ).permitAll()

                        //Acesso autenticado (clientes, garçons, admin)
                        .requestMatchers(HttpMethod.GET,
                                "/categorias/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/funcionarios/**",
                                "/diaSemana/**",
                                "/pedidos/cliente",
                                "/pedidos/gerarPagamento",
                                "/pedidos/possuiPedidoAberto",
                                "/mesa/**",
                                "/produtos/**"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT, "/pedidos/**", "/clientes/**").hasAnyAuthority("CLIENTE", "FUNCIONARIO_ADM", "FUNCIONARIO_GARCOM")

                        .requestMatchers(HttpMethod.POST, "/pedidos/**", "/clientes/**", "/produtoPedido/**", "/avaliacao/insert").hasAnyAuthority("CLIENTE", "FUNCIONARIO_GARCOM")

                        //Acesso do garçom
                        .requestMatchers("/mesa/**").hasAuthority("FUNCIONARIO_GARCOM")

                        .requestMatchers(
                                "/adm/**",
                                "/categorias/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/produtos/**",
                                "/funcionarios/**",
                                "/diaSemana/**",
                                "/avaliacao/**"
                        ).hasAuthority("FUNCIONARIO_ADM")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                );


        // Adiciona o filtro JWT
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200", "http://192.168.15.85:4200"));
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
