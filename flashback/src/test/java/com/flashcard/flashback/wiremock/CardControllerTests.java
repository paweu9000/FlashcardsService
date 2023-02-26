package com.flashcard.flashback.wiremock;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
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

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

public class CardControllerTests {
    private WireMockServer wireMockServer;

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    CardService cardService;
    CollectionEntity collection;
    UsersEntity user;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        user = new UsersEntity();
        user.setId(1L);
        collection = new CollectionEntity();
        collection.setId(1L);
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

        System.out.println(response.getEntity());
    }
}
