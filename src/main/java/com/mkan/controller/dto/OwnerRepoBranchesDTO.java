package com.mkan.controller.dto;

import java.util.List;


public record OwnerRepoBranchesDTO(String login, List<RepoDTO> repositories) {
    public record RepoDTO(String name, List<BranchDTO> branches) {
        public record BranchDTO(String name, String sha) {
        }
    }
}
