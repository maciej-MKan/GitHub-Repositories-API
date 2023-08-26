package com.mkan.integration;

import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.integration.configuration.RestAssuredIntegrationTestBase;
import com.mkan.integration.support.GHControllerTestSupport;
import com.mkan.integration.support.WiremockTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GHControllerIT
        extends RestAssuredIntegrationTestBase
        implements WiremockTestSupport, GHControllerTestSupport {

    @Test
    void thatFindingUsersReposCorrectly() {
        //given
        String login = "test";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForRepos(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        OwnerRepoBranchesDTO expected = getUsersReposAndBranchesCorrectly(someOwner);

        //then
        List<RepoDTO> ownersRepos = expected.getRepositories();
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
        var expected = getWithIncorrectAccept(someOwner);

        //then
        expected.body("status", equalTo(406));
        expected.body("message", notNullValue());

    }

    @Test
    void thatReturnUserNotFoundMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForIncorrectUser(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        var expected = getWithIncorrectLogin(someOwner);

        //then
        expected.body("status", equalTo(404));
        expected.body("message", notNullValue());

    }

    @Test
    void thatReturnForbiddenMessage() {
        //given
        String login = "incorrect";
        OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
        stubForForbiddenAccess(wireMockServer, login);
        stubForBranches(wireMockServer, login);

        //when
        var expected = getWithAccessForbidden(someOwner);

        //then
        expected.body("status", equalTo(403));
        expected.body("message", notNullValue());

    }

}
