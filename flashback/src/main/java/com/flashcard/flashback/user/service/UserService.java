package com.flashcard.flashback.user.service;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataAccessException;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.data.mapper.UserMapper;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.service.EmailService;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService{

    @PersistenceContext
    EntityManager entityManager;

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    VerificationTokenService tokenService;
    EmailService emailService;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       VerificationTokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    public void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
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

    public void register(UserDto userDto) throws MessagingException {
        if(exists(userDto)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User with this credentials already exist!"
            );
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        UsersEntity user = mapDto(userDto);
        save(user);
        VerificationToken token = tokenService.generateVerificationToken(user);
        emailService.sendVerificationEmail(userDto.getEmail(), token.getToken());
    }

    public UserDao toDao(UsersEntity user) {
        return UserMapper.INSTANCE.entityToDao(user);
    }

    public UsersEntity mapDto(UserDto userDto) {
        return UserMapper.INSTANCE.dtoToEntity(userDto);
    }

    public boolean exists(UserDto userDto) {
        if(userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            return true;
        } else if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return true;
        } else return userRepository.findByUsername(userDto.getUsername()).isPresent();
    }

    public UserDao mapUserData(Long id, String loginOrEmail) {
        UsersEntity user = findById(id);
        if (loginOrEmail != null) return toDao(user);
        else throw new UnauthorizedDataAccessException(UserDao.class);
    }

    public UserDao getCurrentUser(String loginOrEmail) {
        UsersEntity user = findByEmailOrLogin(loginOrEmail);
        return toDao(user);
    }

    public void confirmEmail(VerificationToken verificationToken) {
        UsersEntity user = verificationToken.getUsersEntity();
        user.setVerified(true);
        save(user);
    }

    public List<UserDao> searchByUsername(String username) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(UsersEntity.class)
                .get();

        Query luceneQuery = buildLuceneQuery(username, queryBuilder);
        List<UsersEntity> users = getUserListFromQuery(luceneQuery, fullTextEntityManager);

        return users.stream().map(this::toDao).toList();
    }

    private List<UsersEntity> getUserListFromQuery(Query query, FullTextEntityManager entityManager) {
        return entityManager.createFullTextQuery(query, UsersEntity.class).getResultList();
    }

    private Query buildLuceneQuery(String username, QueryBuilder queryBuilder) {
        return queryBuilder
                .keyword()
                .fuzzy()
                .onField("username")
                .matching(username)
                .createQuery();
    }
}
