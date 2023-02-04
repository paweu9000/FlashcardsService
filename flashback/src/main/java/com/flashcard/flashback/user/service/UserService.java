package com.flashcard.flashback.user.service;

import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public record UserService(UserRepository userRepository) {

    public UsersEntity findByEmailOrLogin(String emailOrLogin) {
        UsersEntity optUser =  userRepository.findAll().stream().filter(usersEntity -> usersEntity.getLogin().equals(emailOrLogin) ||
                usersEntity.getEmail().equals(emailOrLogin)).findFirst().get();
        return unwrapUser(optUser);
    }

    private UsersEntity unwrapUser(UsersEntity user) {
        if(user != null) {
            return user;
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }

    public void save(UsersEntity usersEntity) {
        try {
            userRepository.save(usersEntity);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void register(UserDto userDto) {
        UsersEntity user = mapDto(userDto);
        save(user);
    }

    public UserDao toDao(UsersEntity user) {
        return new UserDao(user);
    }

    public UsersEntity mapDto(UserDto userDto) {
        UsersEntity user = new UsersEntity();
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        user.setUsername(userDto.getUsername());
        return user;
    }
}
