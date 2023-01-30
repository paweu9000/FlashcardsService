package com.flashcard.flashback.user.service;

import com.flashcard.flashback.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public record UserService(UserRepository userRepository) {
}
