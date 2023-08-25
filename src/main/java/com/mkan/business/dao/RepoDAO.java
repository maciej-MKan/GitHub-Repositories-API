package com.mkan.business.dao;

import com.mkan.domain.Owner;
import com.mkan.domain.Repo;

import java.util.Optional;

public interface RepoDAO {
    Optional<Repo> getRepoByOwner(Owner owner);
}
