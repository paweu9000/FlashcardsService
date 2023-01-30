package com.flashcard.flashback.user.controller;

import com.flashcard.flashback.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public record UserController(UserService userService) {
}
