package com.mkan.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.mkan.controller.dto.OwnerDTO;
import com.mkan.controller.dto.OwnerRepoBranchesDTO;
import com.mkan.business.model.Repo;
import org.junit.jupiter.api.*;
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
import static com.mkan.controller.rest.GHController.API_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ApplicationEndToEndTest implements WiremockStubs {

    private static WebTestClient.RequestBodySpec requestBodySpec;
    private static WireMockServer wireMockServer;
    @LocalServerPort
    private int port;

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
    void thatFindsUsersReposCorrectly() {
        //given
        String login = "test";
        OwnerDTO someOwner = new OwnerDTO(login);
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
        List<Repo> ownersRepos = Objects.requireNonNull(expected.getResponseBody()).repositories();

        assertThat(ownersRepos).hasSize(10);

        ownersRepos.forEach(repo -> assertThat(repo.branches()).isNotEmpty());
        ownersRepos.forEach(repo -> repo.branches()
                .forEach(branch -> assertThat(branch.commit().sha()).isNotEmpty()));

    }

    @Test
    void thatReturnsNotAcceptableMessage() {
        //given
        String login = "test";
        OwnerDTO someOwner = new OwnerDTO(login);
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
    void thatReturnsUserNotFoundMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = new OwnerDTO(login);
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
    void thatReturnsForbiddenMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = new OwnerDTO(login);
        stubForForbiddenAccess(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        WebTestClient.BodyContentSpec expected = requestBodySpec
                .accept(MediaType.APPLICATION_XML)
                .bodyValue(someOwner)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();

        //then
        expected
                .jsonPath("status").isEqualTo(500)
                .jsonPath("message").isNotEmpty();
    }
}
