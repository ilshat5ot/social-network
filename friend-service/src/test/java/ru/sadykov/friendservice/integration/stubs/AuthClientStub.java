package ru.sadykov.friendservice.integration.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class AuthClientStub {

    public static void stubAuthCallResponseTrue(long userId) {
        stubFor(get(urlEqualTo(String.format("/api/v1/auth/%d", userId)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
    }

    public static void stubAuthCallResponseFalse(long userId) {
        stubFor(get(urlEqualTo(String.format("/api/v1/auth/%d", userId)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));
    }
}
