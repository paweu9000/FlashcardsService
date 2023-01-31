package com.flashcard.flashback.user.service;

import com.flashcard.flashback.user.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public record UserService(UserRepository userRepository) {

    public UsersEntity findByEmailOrLogin(String emailOrLogin) {
        UsersEntity optUser =  userRepository.findAll().stream().filter(usersEntity -> usersEntity.getLogin().equals(emailOrLogin) ||
                usersEntity.getEmail().equals(emailOrLogin)).findFirst().get();
        return unwrapUser(optUser);
    }

    public UsersEntity unwrapUser(UsersEntity user) {
        if(user != null) {
            return user;
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }
}
