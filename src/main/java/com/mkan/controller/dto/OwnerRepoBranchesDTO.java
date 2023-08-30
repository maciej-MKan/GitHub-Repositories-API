package com.mkan.controller.dto;

import com.mkan.business.model.Repo;

import java.util.List;


public record OwnerRepoBranchesDTO(String login, List<Repo>repositories) {}
