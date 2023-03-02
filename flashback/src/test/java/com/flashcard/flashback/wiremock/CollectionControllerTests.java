package com.flashcard.flashback.wiremock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.card.entity.CardEntity;
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

import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CollectionControllerTests {

    private WireMockServer wireMockServer;

    @Mock
    CollectionRepository collectionRepository;

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
        collection.setTitle("Test");
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
}
