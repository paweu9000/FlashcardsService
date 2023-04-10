package com.flashcard.flashback.security;

import com.flashcard.flashback.exception.UnverifiedEmailException;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
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
class CustomAuthenticationManager implements AuthenticationManager {

    UserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsersEntity user = userService.findByEmailOrLogin(authentication.getName());
        if(!user.isVerified()) throw new UnverifiedEmailException();
        if(!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("The password is invalid");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }
}
