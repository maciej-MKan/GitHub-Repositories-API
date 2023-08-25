package com.mkan.api.dto.mapper;

import com.mkan.api.dto.OwnerDTO;
import com.mkan.domain.Owner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner map(OwnerDTO ownerDTO);
}
