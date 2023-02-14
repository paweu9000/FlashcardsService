package com.flashcard.flashback.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UsersEntity user = new ObjectMapper().readValue(request.getInputStream(), UsersEntity.class);
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.attemptAuthentication(request, response);
    }
}
