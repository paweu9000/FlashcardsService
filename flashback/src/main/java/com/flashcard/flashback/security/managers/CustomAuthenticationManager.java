package com.flashcard.flashback.security.managers;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsersEntity user = null;
        user = userService.findByEmailOrLogin(authentication.getName());
        if(!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("The password is invalid");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }
}
