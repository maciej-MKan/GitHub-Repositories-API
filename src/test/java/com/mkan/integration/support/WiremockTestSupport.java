package com.mkan.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Map;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
public interface WiremockTestSupport {

    Map<String, String> BRANCHES_NAMES = Map.of(
            "Browser_Calculator_Django" , "Repo1",
            "car_dealership" , "Repo2",
            "drf_projects_api" , "Repo3",
            "GitHub-Repositories-API" , "Repo4",
            "maciej-MKan" , "Repo5",
            "Music_Player_Django" , "Repo6",
            "projects-manager" , "Repo7",
            "projects-manager-pyramid-api" , "Repo8",
            "projects-manager-spring-api" , "Repo9",
            "WebAPI4BlackJack" , "Repo10"

    );

    default void stubForRepos(final WireMockServer wireMockServer, final String ownerLogin) {
        wireMockServer.stubFor(get(urlPathEqualTo("/users/%s/repos".formatted(ownerLogin)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock/reposByOwnerLogin.json")
                        .withTransformerParameters(Map.of("login", ownerLogin))
                        .withTransformers("response-template")
                ));
    }

    default void stubForBranches(final WireMockServer wireMockServer, final String ownerLogin){
        BRANCHES_NAMES.forEach((repoName, fileName) ->
        wireMockServer.stubFor(get(urlPathEqualTo("/repos/maciej-MKan/%s/branches".formatted(repoName)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock/branchFor%s.json".formatted(fileName))
//                        .withTransformerParameters(Map.of("login", ownerLogin))
                        .withTransformers("response-template")
                )));
    }
}
