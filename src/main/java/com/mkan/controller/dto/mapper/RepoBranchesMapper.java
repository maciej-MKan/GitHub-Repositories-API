package com.mkan.controller.dto.mapper;

import com.mkan.controller.dto.OwnerDTO;
import com.mkan.controller.dto.OwnerRepoBranchesDTO;
import com.mkan.model.Repo;

import java.util.List;

public class RepoBranchesMapper {
    public static OwnerRepoBranchesDTO map(OwnerDTO ownerDTO, List<Repo> repositories) {
        List<OwnerRepoBranchesDTO.RepoDTO> repoDTOS = repositories.stream()
                .map(repo -> new OwnerRepoBranchesDTO.RepoDTO(repo.name(),
                        repo.branches().stream()
                                .map(branch -> new OwnerRepoBranchesDTO.RepoDTO.BranchDTO(
                                        branch.name(),
                                        branch.commit().sha()
                                ))
                                .toList()
                ))
                .toList();

        return new OwnerRepoBranchesDTO(ownerDTO.login(), repoDTOS);
    }
}
