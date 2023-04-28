# FlashcardsService

FlashcardsService is a backend for Flashcards Application.
The App allows user to create their own sets of flashcards and tests.
Once created the sets can be easily shared and accessed by other users.

## Installation
(The Application will be fully dockerized in the future to skips all the steps below)
1. Create ENV variable SECRET_KEY and put there key that can be generated here 'https://jwt.io/'
2. Create resources package in monolith/src/main/java package and add there application.yml file with content shown below (Fill everything with your data)
```yml
spring:

  datasource:
    url: YOUR ADDRESS
    username: YOUR USERNAME
    password: YOUR PASSWORD
    driver-class-name=com: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

  mail:
    host: smtp.gmail.com
    port: 587
    username: YOUR USERNAME
    password: YOUR PASSWORD
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

server:
  port : 3000
```
