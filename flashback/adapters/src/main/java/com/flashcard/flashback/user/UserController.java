package com.flashcard.flashback.user;

import com.flashcard.flashback.user.data.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
record UserController(UserService userService) {

    @GetMapping("/{id}")
    ResponseEntity<UserDao> getUserData(@PathVariable Long id,
                                               @CurrentSecurityContext
                                                       (expression = "authentication?.name") String nameOrEmail) {
        return new ResponseEntity<>(userService.mapUserData(id, nameOrEmail), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<UserDao> getCurrentUser(@CurrentSecurityContext
                                                              (expression = "authentication?.name") String nameOrEmail) {
        return new ResponseEntity<>(userService.getCurrentUser(nameOrEmail), HttpStatus.OK);
    }

    @GetMapping("/search/{username}")
    ResponseEntity<List<UserDao>> findUsers(@PathVariable String username) {
        return new ResponseEntity<>(userService.searchByUsername(username), HttpStatus.OK);
    }
}
