package com.flashcard.flashback.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.test.data.TestDao;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestControllerTests {

    private WireMockServer wireMockServer;
    private TestDao testDao;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        testDao = new TestDao();
        testDao.setId(1L);
        testDao.setCollectionId(1L);
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getTestSortedOrderTest() throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(testDao);

        stubFor(get(urlEqualTo("/api/test/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/test/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(body, response.body());
    }

    @Test
    public void getTestRandomOrderTest() throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(testDao);

        stubFor(get(urlEqualTo("/api/test/1/random"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/test/1/random"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(body, response.body());
    }
}
