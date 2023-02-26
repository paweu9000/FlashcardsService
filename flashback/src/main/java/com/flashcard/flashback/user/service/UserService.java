package com.flashcard.flashback.user.service;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UsersEntity findByEmailOrLogin(String emailOrLogin) throws EntityNotFoundException {
        Optional<UsersEntity> user;
        if(checkEmail(emailOrLogin)) {
            user = userRepository.findByEmail(emailOrLogin);
        } else {
            user = userRepository.findByLogin(emailOrLogin);
        }
        return unwrapUser(user);
    }

    public UsersEntity findById(Long id) {
        return unwrapUser(userRepository.findById(id));
    }

    private UsersEntity unwrapUser(Optional<UsersEntity> user) {
        if(user.isPresent()) return user.get();
        else throw new EntityNotFoundException(UsersEntity.class);
    }

    public boolean checkEmail(String email) {
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
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
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
