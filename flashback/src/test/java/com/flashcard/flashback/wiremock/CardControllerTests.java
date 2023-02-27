package com.flashcard.flashback.wiremock;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.user.entity.UsersEntity;
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

import java.io.IOException;
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

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService.setCollectionService(collectionService);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        user = new UsersEntity();
        user.setId(1L);
        user.setLogin("login");
        user.setEmail("email@example.com");
        collection = new CollectionEntity();
        collection.setId(1L);
        user.addCollection(collection);
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
    }


}