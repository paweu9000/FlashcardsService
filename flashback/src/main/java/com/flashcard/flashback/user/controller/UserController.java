package com.flashcard.flashback.user.controller;

import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public record UserController(UserService userService) {

    @GetMapping("/{id}")
    public ResponseEntity<UserDao> getUserData(@PathVariable Long id,
                                               @CurrentSecurityContext
                                                       (expression = "authentication?.name") String nameOrEmail) {
        return new ResponseEntity<>(userService.mapUserData(id, nameOrEmail), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDao> getCurrentUser(@CurrentSecurityContext
                                                              (expression = "authentication?.name") String nameOrEmail) {
        return new ResponseEntity<>(userService.getCurrentUser(nameOrEmail), HttpStatus.OK);
    }
}
