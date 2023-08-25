package com.mkan.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Map;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
public interface WiremockTestSupport {

    default void stubForRepos(final WireMockServer wireMockServer, final String ownerLogin) {
        wireMockServer.stubFor(get(urlPathEqualTo("/users/%s/repos".formatted(ownerLogin)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock/reposByOwnerLogin.json")
                        .withTransformerParameters(Map.of("login", ownerLogin))
                        .withTransformers("response-template")));
    }
}
