package com.mkan.business.dao;

import com.mkan.domain.Branch;

import java.util.List;

public interface BranchDAO {

    List<Branch> findBranchesByRepoName(String name);
}
