package com.flashcard.flashback.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.user.data.UserDao;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTests {

    private WireMockServer wireMockServer;
    private UserDao userDao;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUsername("username");
        userDao.setCollections(new ArrayList<>());
        userDao.setSavedCollections(new ArrayList<>());
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getUserDataValidTest() throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(userDao);

        stubFor(get(urlEqualTo("/api/user/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/user/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(body, response.body());
    }

    @Test
    public void getUserDataInvalidTest() throws IOException, InterruptedException {
        String body = "You are not authorized to view entity: " + UserDao.class.getSimpleName();
        stubFor(get(urlEqualTo("/api/user/1"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/user/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        assertEquals(body, response.body());
    }

    @Test
    public void getUserDataTest() throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(userDao);

        stubFor(get(urlEqualTo("/api/user"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/user"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(body, response.body());
    }

    @Test
    public void searchUsersByUsername() throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(Arrays.asList(userDao));

        stubFor(get(urlEqualTo("/api/user/search/username"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/user/search/username"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(body, response.body());
    }
}
