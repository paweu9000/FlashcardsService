package com.flashcard.flashback.user.service;

import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public record UserService(UserRepository userRepository) {

    public UsersEntity findByEmailOrLogin(String emailOrLogin) {
        Optional<UsersEntity> user;
        if(checkEmail(emailOrLogin)) {
            user = userRepository.findByEmail(emailOrLogin);
        } else {
            user = userRepository.findByLogin(emailOrLogin);
        }
        return unwrapUser(user.get());
    }

    private UsersEntity unwrapUser(UsersEntity user) {
        if(user != null) {
            return user;
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }

    private boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void save(UsersEntity usersEntity) {
        try {
            userRepository.save(usersEntity);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void register(UserDto userDto) {
        if(exists(userDto)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User with this credentials already exist!"
            );
        }
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

    public boolean exists(UserDto userDto) {
        if(userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            return true;
        } else if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return true;
        } else return userRepository.findByUsername(userDto.getUsername()).isPresent();
    }
}
