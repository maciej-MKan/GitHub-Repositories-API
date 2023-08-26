package com.mkan.unit.service;

import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.api.dto.mapper.OwnerMapper;
import com.mkan.api.dto.mapper.RepoMapper;
import com.mkan.business.GHService;
import com.mkan.business.dao.BranchDAO;
import com.mkan.business.dao.RepoDAO;
import com.mkan.domain.Branch;
import com.mkan.domain.Owner;
import com.mkan.domain.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GHServiceTest {

    @Mock
    private RepoDAO repoDAO;

    @Mock
    private BranchDAO branchDAO;

    @Mock
    private OwnerMapper ownerMapper;

    @Mock
    private RepoMapper repoMapper;

    private GHService ghService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ghService = new GHService(repoDAO, branchDAO, ownerMapper, repoMapper);
    }

    @Test
    void testFindOwnerReposAndBranches() {
        OwnerDTO ownerDTO = new OwnerDTO();
        Owner owner = Owner.builder().build();
        when(ownerMapper.map(ownerDTO)).thenReturn(owner);

        Repo repo1 = Repo.builder().name("repo1").fork(false).build();
        Repo repo2 = Repo.builder().name("repo2").fork(true).build();
        List<Repo> repos = List.of(repo1, repo2);
        when(repoDAO.findReposByOwnerLogin(owner.getLogin())).thenReturn(repos);

        List<Branch> branches1 = List.of(Branch.builder().build());
        List<Branch> branches2 = List.of(Branch.builder().build());
        when(branchDAO.findBranchesByRepoName("repo1")).thenReturn(branches1);
        when(branchDAO.findBranchesByRepoName("repo2")).thenReturn(branches2);

        RepoDTO repoDTO1 = new RepoDTO();
        RepoDTO repoDTO2 = new RepoDTO();
        when(repoMapper.map(repo1)).thenReturn(repoDTO1);
        when(repoMapper.map(repo2)).thenReturn(repoDTO2);

        OwnerRepoBranchesDTO expectedResult = OwnerRepoBranchesDTO.builder()
                .repositories(List.of(repoDTO1))
                .build();

        OwnerRepoBranchesDTO result = ghService.findOwnerReposAndBranches(ownerDTO);

        assertEquals(expectedResult, result);

        verify(ownerMapper, times(1)).map(ownerDTO);
        verify(repoDAO, times(1)).findReposByOwnerLogin(owner.getLogin());
        verify(branchDAO, times(1)).findBranchesByRepoName("repo1");
        verify(repoMapper, times(1)).map(any(Repo.class));
    }
}