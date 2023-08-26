package com.mkan.business;

import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.mapper.OwnerMapper;
import com.mkan.api.dto.mapper.RepoMapper;
import com.mkan.business.dao.BranchDAO;
import com.mkan.business.dao.RepoDAO;
import com.mkan.domain.Branch;
import com.mkan.domain.Owner;
import com.mkan.domain.Repo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GHService {

    private final RepoDAO repoDAO;
    private final BranchDAO branchDAO;
    private final OwnerMapper ownerMapper;
    private final RepoMapper repoMapper;

    @Transactional
    public OwnerRepoBranchesDTO findOwnerReposAndBranches(OwnerDTO ownerDTO) {

        List<Repo> repos = getOwnerRepos(ownerMapper.map(ownerDTO)).stream()
                .peek(repo -> log.info("Found repo [{}]", repo))
                .filter(repo -> !repo.getFork())
                .toList();
        log.info("Founded repos after drop forks [{}]", repos);
        List<Repo> reposWithBranches = repos.stream().map(repo -> repo.withBranches(getReposBranches(repo))).toList();
        log.info("Founded repos with branches [{}]", reposWithBranches);

        return getOwnerRepoBranchesDTO(reposWithBranches, ownerDTO.getLogin());
    }

    private OwnerRepoBranchesDTO getOwnerRepoBranchesDTO(List<Repo> repos, String login) {
        return OwnerRepoBranchesDTO.builder().login(login).repositories(repos.stream()
                        .map(repoMapper::map)
                        .toList())
                .build();
    }

    private List<Branch> getReposBranches(Repo repo) {
        return branchDAO.findBranchesByRepoName(repo.getName());
    }

    private List<Repo> getOwnerRepos(Owner owner) {
        return repoDAO.findReposByOwnerLogin(owner.getLogin());
    }
}
