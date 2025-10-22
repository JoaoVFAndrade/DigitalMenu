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
                                "/recuperacao/**"
                        ).permitAll()

                        //Acesso autenticado (clientes, garçons, admin)
                        .requestMatchers(HttpMethod.GET,
                                "/produtos/**",
                                "/categorias/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/funcionarios/**",
                                "/diaSemana/**",
                                "/pedidos/cliente"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT,"/pedidos/**","/clientes/**" ).hasAuthority("CLIENTE")

                        .requestMatchers(HttpMethod.POST,"/pedidos/**","/clientes/**","/produtoPedido/**" ).hasAuthority("CLIENTE")

                        //Acesso do garçom
                        .requestMatchers("/pedidos/abertos").hasAuthority("FUNCIONARIO_GARCOM")
                            //@TODO verificar quais rotas o garcom podera acessar
                        //Acesso do administrador (CRUD completo)
                        .requestMatchers(
                                "/adm/**",
                                "/categorias/**",
                                "/ingredientes/**",
                                "/restricoes/**",
                                "/produtos/**",
                                "/funcionarios/**",
                                "/diaSemana/**"
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
