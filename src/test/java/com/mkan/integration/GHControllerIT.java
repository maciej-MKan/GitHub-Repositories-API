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
        OwnerRepoBranchesDTO expected = getUsersReposAndBranches(someOwner);

        //then
        List<RepoDTO> ownersRepos = expected.getOwnersRepos();
        assertThat(ownersRepos).hasSize(10);

        ownersRepos.forEach(repoDTO -> assertThat(repoDTO.getBranches()).hasSize(1));
        ownersRepos.forEach(repoDTO -> repoDTO.getBranches()
                .forEach(branchDTO -> assertThat(branchDTO.getSha()).isNotEmpty()));

    }

}
