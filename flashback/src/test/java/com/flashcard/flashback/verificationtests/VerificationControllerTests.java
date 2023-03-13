package com.flashcard.flashback.verificationtests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;

public class VerificationControllerTests {

    private WireMockServer wireMockServer;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }
}
