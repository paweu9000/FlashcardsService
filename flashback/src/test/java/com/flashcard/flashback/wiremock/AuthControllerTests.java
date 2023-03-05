package com.flashcard.flashback.wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.user.data.UserDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerTests {

    private WireMockServer wireMockServer;
    private UserDto userDto;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        userDto = UserDto.builder()
                .username("username")
                .login("login")
                .email("email@example.com")
                .password("password")
                .build();
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void registerUserTest() throws IOException, InterruptedException {
        stubFor(post(urlEqualTo("/api/auth/register"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(userDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/auth/register")));
    }

    @Test
    public void registerUserInvalidRequestTest() throws IOException, InterruptedException {
        userDto.setPassword("pas");
        userDto.setEmail("invalid email");
        stubFor(post(urlEqualTo("/api/auth/register"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(userDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/auth/register")));
    }
}
