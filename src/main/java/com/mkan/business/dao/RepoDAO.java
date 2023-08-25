package com.mkan.business.dao;

import com.mkan.domain.Owner;
import com.mkan.domain.Repo;

import java.util.List;
import java.util.Optional;

public interface RepoDAO {

    List<Repo> findReposByOwnerLogin(String login);
}
