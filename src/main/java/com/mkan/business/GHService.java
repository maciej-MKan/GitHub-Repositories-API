package com.mkan.business;

import com.mkan.api.dto.BranchDTO;
import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.api.dto.mapper.BranchMapper;
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
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class GHService {

    private final RepoDAO repoDAO;
    private final BranchDAO branchDAO;
    private final OwnerMapper ownerMapper;
    private final BranchMapper branchMapper;
    private final RepoMapper repoMapper;

    @Transactional
    public OwnerRepoBranchesDTO findOwnerReposAndBranches(OwnerDTO ownerDTO) {

        List<Repo> repos = getOwnerRepos(ownerMapper.map(ownerDTO)).stream()
                .filter(repo -> !repo.getFork())
                .map(repo -> Repo.builder()
                        .name(repo.getName())
                        .fork(false)
                        .default_branch(repo.getDefault_branch())
                        .Branches(getReposBranches(repo))
                        .build())
                .toList();

        return OwnerRepoBranchesDTO.builder().ownersRepos(repos.stream().map(repoMapper::map).toList()).build();
    }

    private List<Branch> getReposBranches(Repo repo) {
        List<Branch> branchesByRepoName = branchDAO.findBranchesByRepoName(repo.getName(), repo.getDefault_branch());
        return branchesByRepoName;
    }

    private List<Repo> getOwnerRepos(Owner owner) {

        return repoDAO.findReposByOwnerLogin(owner.getLogin());
    }
}
