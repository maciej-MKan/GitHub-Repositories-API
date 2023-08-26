package com.mkan.api.dto.mapper;

import com.mkan.api.dto.BranchDTO;
import com.mkan.domain.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {

    @Mapping(source = "commit.sha", target = "sha")
    BranchDTO map(Branch branch);


}
