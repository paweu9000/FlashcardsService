package com.flashcard.flashback.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionRepository;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.user.UsersEntity;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CardControllerTests {
    private WireMockServer wireMockServer;

    @Mock
    CardRepository cardRepository;

    @Mock
    CollectionRepository collectionRepository;

    @InjectMocks
    @Spy
    CollectionService collectionService;

    @InjectMocks
    CardService cardService;
    CollectionEntity collection;
    UsersEntity user;
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService.setCollectionService(collectionService);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        objectMapper = new ObjectMapper();
        user = new UsersEntity();
        user.setId(1L);
        user.setLogin("login");
        user.setEmail("email@example.com");
        collection = new CollectionEntity();
        collection.setId(1L);
        user.addCollection(collection);
        collection.setOwners(user);
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetCardRequest() throws IOException {
        CardEntity card = new CardEntity(1L, "Side", "Value", collection, user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        stubFor(get(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8080/api/cards/1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(200, response.getCode());
        verify(getRequestedFor(urlEqualTo("/api/cards/1")));
    }

    @Test
    public void testGetCardErrorRequest() throws IOException {
        stubFor(get(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(404)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8080/api/cards/1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(404, response.getCode());
        verify(getRequestedFor(urlEqualTo("/api/cards/1")));
    }

    @Test
    public void testGetAllCardsRequest() throws IOException {
        CardEntity card1 = new CardEntity(1L, "Side", "Value", collection, user);
        CardEntity card2 = new CardEntity(2L, "Side", "Value", collection, user);
        CardEntity card3 = new CardEntity(3L, "Side", "Value", collection, user);
        collection.addCard(card1);
        collection.addCard(card2);
        collection.addCard(card3);

        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        stubFor(get(urlEqualTo("/api/cards/all?=1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8080/api/cards/all?=1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(200, response.getCode());
        verify(getRequestedFor(urlEqualTo("/api/cards/all?=1")));
    }

    @Test
    public void testDeleteCardWithAnonymousUser() throws IOException {
        CardEntity card = new CardEntity(1L, "Side", "Value", collection, user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        stubFor(delete(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(401)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete("http://localhost:8080/api/cards/1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(401, response.getCode());
        verify(deleteRequestedFor(urlEqualTo("/api/cards/1")));
    }

    @Test
    public void testDeleteCardWithValidUser() throws IOException {
        CardEntity card = new CardEntity(1L, "Side", "Value", collection, user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        stubFor(delete(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(200)));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete("http://localhost:8080/api/cards/1");
        CloseableHttpResponse response = client.execute(request);

        assertEquals(200, response.getCode());
        verify(deleteRequestedFor(urlEqualTo("/api/cards/1")));
    }

    @Test
    public void testPostCardWithValidUser() throws IOException, InterruptedException {
        CardDto cardDto = new CardDto();
        cardDto.setCollectionId(1L);
        cardDto.setCreatorId(1L);
        cardDto.setValue("Value");
        cardDto.setSide("Side");

        stubFor(post(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(cardDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/cards/1"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/cards/1")));
    }

    @Test
    public void testPostCardWithInvalidUser() throws IOException, InterruptedException {
        CardDto cardDto = new CardDto();
        cardDto.setCollectionId(1L);
        cardDto.setCreatorId(1L);
        cardDto.setValue("Value");
        cardDto.setSide("Side");

        stubFor(post(urlEqualTo("/api/cards/1"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("")));

        String body = objectMapper.writeValueAsString(cardDto);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/cards/1"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        verify(postRequestedFor(urlEqualTo("/api/cards/1")));
    }
}
