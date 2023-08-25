package com.mkan.infrastructure.GitHub.api;

import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
public abstract class AbstractGHAPI {

    static final String GH_BRANCHES = "repos/{owner_login}/{repo_name}/branches";
    static final String GH_REPOS = "users/{owner_login}/repos";
    protected final WebClient webClient;
}
