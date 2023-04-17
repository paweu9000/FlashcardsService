package com.flashcard.flashback.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcard.flashback.test.data.TestDao;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;

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
}
