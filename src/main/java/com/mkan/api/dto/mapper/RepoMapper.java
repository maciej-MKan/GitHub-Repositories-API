package com.mkan.api.dto.mapper;

import com.mkan.api.dto.RepoDTO;
import com.mkan.domain.Repo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RepoMapper {

    RepoDTO map(Repo repo);
    Repo map(RepoDTO repo);
}
