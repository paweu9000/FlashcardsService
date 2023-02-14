package com.flashcard.flashback.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    AuthenticationManager authenticationManager;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/authenticate");

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                return config;
            }
        }).and().csrf().disable().authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
