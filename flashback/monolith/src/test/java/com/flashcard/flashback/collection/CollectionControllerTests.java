package com.flashcard.flashback.collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.user.UserRepository;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CollectionControllerTests {

    private WireMockServer wireMockServer;

    @Mock
    CollectionRepository collectionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    @Spy
    UserService userService;

    @InjectMocks
    CollectionService collectionService;
    CollectionEntity collection;
    UsersEntity user;
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        objectMapper = new ObjectMapper();
        user = new UsersEntity();
        user.setId(1L);
        user.setLogin("login");
        user.setEmail("email@example.com");
        collection = new CollectionEntity();
        collection.setId(1L);
        collection.setTitle("title");
        user.addCollection(collection);
        collection.setOwners(user);
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetCollectionRequest() throws IOException {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        stubFor(get(urlEqualTo("/api/collection/1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8080/api/collection/1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(200, response.getCode());
        verify(getRequestedFor(urlEqualTo("/api/collection/1")));
    }

    @Test
    @WithMockUser(username = "email@example.com")
    public void testPostCollectionRequestWithValidUser() throws IOException, InterruptedException {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setTitle("Title");

        stubFor(post(urlEqualTo("/api/collection"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(collectionDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/collection")));
    }

    @Test
    @WithAnonymousUser
    public void testPostCollectionRequestWithAnonymousUser() throws IOException, InterruptedException {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setTitle("Title");

        stubFor(post(urlEqualTo("/api/collection"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(collectionDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/collection")));
    }

    @Test
    @WithMockUser(username = "email@example.com")
    public void testDeleteCollectionRequestWithValidUser() throws IOException, InterruptedException {

        stubFor(delete(urlEqualTo("/api/collection/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        verify(deleteRequestedFor(urlEqualTo("/api/collection/1")));
    }

    @Test
    @WithAnonymousUser
    public void testDeleteCollectionRequestWithAnonymousUser() throws IOException, InterruptedException {

        stubFor(delete(urlEqualTo("/api/collection/1"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("")));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        verify(deleteRequestedFor(urlEqualTo("/api/collection/1")));
    }

    @Test
    @WithAnonymousUser
    public void testUpvoteCollectionRequestWithAnonymousUser() throws IOException, InterruptedException {

        stubFor(get(urlEqualTo("/api/collection/1/like"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("")));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection/1/like"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        verify(getRequestedFor(urlEqualTo("/api/collection/1/like")));
    }

    @Test
    @WithMockUser(username = "login")
    public void testUpvoteCollectionRequestWithValidUser() throws IOException, InterruptedException {
        stubFor(get(urlEqualTo("/api/collection/1/like"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection/1/like"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        verify(getRequestedFor(urlEqualTo("/api/collection/1/like")));
    }

    @Test
    public void findByTitleNotFoundTest() throws IOException, InterruptedException {
        stubFor(get(urlEqualTo("/api/collection/search/title"))
                .willReturn(aResponse()
                        .withStatus(404)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/collection/search/title"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        verify(getRequestedFor(urlEqualTo("/api/collection/search/title")));
    }

}
