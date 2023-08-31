package com.mkan.integration;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public interface WiremockStubs {

    Map<String, String> BRANCHES_NAMES = Map.of(
            "Browser_Calculator_Django", "Repo1",
            "car_dealership", "Repo2",
            "drf_projects_api", "Repo3",
            "GitHub-Repositories-API", "Repo4",
            "maciej-MKan", "Repo5",
            "Music_Player_Django", "Repo6",
            "projects-manager", "Repo7",
            "projects-manager-pyramid-api", "Repo8",
            "projects-manager-spring-api", "Repo9",
            "WebAPI4BlackJack", "Repo10"

    );

    default void stubForRepos(final WireMockServer wireMockServer, final String ownerLogin) {
        wireMockServer.stubFor(get(urlPathEqualTo("/users/%s/repos".formatted(ownerLogin)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("stub/reposByOwnerLogin.json")
                        .withTransformerParameters(Map.of("login", ownerLogin))
                        .withTransformers("response-template")
                ));
    }

    default void stubForBranches(final WireMockServer wireMockServer, final String ownerLogin) {
        BRANCHES_NAMES.forEach((repoName, fileName) ->
                wireMockServer.stubFor(get(urlPathEqualTo("/repos/%s/%s/branches".formatted(ownerLogin, repoName)))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("stub/branchFor%s.json".formatted(fileName))
                                .withTransformers("response-template")
                        )));
    }

    default void stubForIncorrectUser(final WireMockServer wireMockServer, final String ownerLogin) {
        wireMockServer.stubFor(get(urlPathEqualTo("/users/%s/repos".formatted(ownerLogin)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)
                        .withBodyFile("stub/notFoundUser.json")
                        .withTransformers("response-template")
                ));
    }

    default void stubForEmptyReposList(final WireMockServer wireMockServer, final String ownerLogin) {
        wireMockServer.stubFor(get(urlPathEqualTo("/users/%s/repos".formatted(ownerLogin)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("[]")
                ));
    }
}
