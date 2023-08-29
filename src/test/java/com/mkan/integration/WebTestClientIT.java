package com.mkan.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.integration.support.WiremockTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.mkan.api.controller.rest.GHController.API_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class WebTestClientIT implements WiremockTestSupport {

    private static WebTestClient.RequestBodySpec requestBodySpec;
    private static WireMockServer wireMockServer;
    @LocalServerPort
    private int port;
    @Value("${api.GitHub.url}")
    private String apiPath;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(
                wireMockConfig()
                        .port(9999)
                        .extensions(new ResponseTemplateTransformer(false))
        );
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setup() {
        requestBodySpec = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .post()
                .uri(API_PATH + "/")
                .contentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void thatFindingUsersReposCorrectly() {
        //given
        String login = "test";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForRepos(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        EntityExchangeResult<OwnerRepoBranchesDTO> expected = requestBodySpec
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(someOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerRepoBranchesDTO.class)
                .returnResult();

        //then
        List<RepoDTO> ownersRepos = Objects.requireNonNull(expected.getResponseBody()).getRepositories();

        assertThat(ownersRepos).hasSize(10);

        ownersRepos.forEach(repoDTO -> assertThat(repoDTO.getBranches()).isNotEmpty());
        ownersRepos.forEach(repoDTO -> repoDTO.getBranches()
                .forEach(branchDTO -> assertThat(branchDTO.getSha()).isNotEmpty()));

    }

    @Test
    void thatReturnNotAcceptableMessage() {
        //given
        String login = "test";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForRepos(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        WebTestClient.BodyContentSpec expected = requestBodySpec
                .accept(MediaType.APPLICATION_XML)
                .bodyValue(someOwner)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody();
        //then
        expected
                .jsonPath("status").isEqualTo(406)
                .jsonPath("message").isNotEmpty();

    }

    @Test
    void thatReturnUserNotFoundMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForIncorrectUser(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        WebTestClient.BodyContentSpec expected = requestBodySpec
                .accept(MediaType.APPLICATION_XML)
                .bodyValue(someOwner)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody();

        //then
        expected
                .jsonPath("status").isEqualTo(404)
                .jsonPath("message").isNotEmpty();
    }
    @Test
    void thatReturnForbiddenMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForForbiddenAccess(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        WebTestClient.BodyContentSpec expected = requestBodySpec
                .accept(MediaType.APPLICATION_XML)
                .bodyValue(someOwner)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody();

        //then
        expected
                .jsonPath("status").isEqualTo(403)
                .jsonPath("message").isNotEmpty();
    }
}
