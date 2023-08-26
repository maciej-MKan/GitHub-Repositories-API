package com.mkan.api.dto.mapper;

import com.mkan.api.dto.BranchDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.domain.Branch;
import com.mkan.domain.Repo;
import lombok.AllArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepoMapper {

    BranchMapper branchesMapper = Mappers.getMapper(BranchMapper.class);

    @Mapping(target = "branches", qualifiedByName = "branchesListMapper")
    RepoDTO map(Repo repo);

    Repo map(RepoDTO repo);

    @Named("branchesListMapper")
    default List<BranchDTO> branchesListMapper(List<Branch> branches) {
        return branches.stream().map(branchesMapper::map).toList();
    }

}
