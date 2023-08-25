package com.mkan.api.dto.mapper;

import com.mkan.api.dto.BranchDTO;
import com.mkan.domain.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchDTO map(Branch reposBranches);
}
