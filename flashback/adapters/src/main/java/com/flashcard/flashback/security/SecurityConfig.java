package com.flashcard.flashback.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@AllArgsConstructor
class SecurityConfig {

    AuthenticationManager authenticationManager;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/authenticate");

        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                config.setMaxAge(3600L);
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "Authorization"));
                config.setExposedHeaders(Collections.singletonList("Authorization"));
                return config;
            }
        }).and().csrf().disable().authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.CARDS_PATH).permitAll()
                .antMatchers(HttpMethod.DELETE, SecurityConstants.CARDS_PATH).authenticated()
                .antMatchers(HttpMethod.POST, SecurityConstants.CARDS_PATH).authenticated()
                .antMatchers(HttpMethod.POST, SecurityConstants.VERIFY_PATH).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.COLLECTION_PATH).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.COLLECTION_PATH).authenticated()
                .antMatchers(HttpMethod.DELETE, SecurityConstants.COLLECTION_PATH).authenticated()
                .antMatchers(HttpMethod.DELETE, SecurityConstants.USER_PATH).authenticated()
                .antMatchers(HttpMethod.POST, SecurityConstants.USER_PATH).authenticated()
                .antMatchers(HttpMethod.GET, SecurityConstants.USER_PATH).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
