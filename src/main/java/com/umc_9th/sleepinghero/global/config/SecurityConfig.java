package com.umc_9th.sleepinghero.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] swaggerUris = {
            "/swagger-ui/**", "/swagger-ui.html",
            "/api-docs/**", "/swagger-resources/**",
            "/webjars/**", "/test/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Swagger 관련만 일단 허용
                        .requestMatchers(swaggerUris).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

