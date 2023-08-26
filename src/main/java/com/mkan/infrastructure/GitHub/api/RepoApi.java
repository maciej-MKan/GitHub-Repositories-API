package com.mkan.infrastructure.GitHub.api;

import com.mkan.business.dao.RepoDAO;
import com.mkan.domain.Repo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RepoApi implements RepoDAO {

    private static final String GH_REPOS = "users/{owner_login}/repos";
    private final WebClient webClient;

    @Override
    public List<Repo> findReposByOwnerLogin(String login) {
        return getRepoByOwnerLogin(login).orElseThrow();
    }

    private Optional<List<Repo>> getRepoByOwnerLogin(String login) {
        try {
            List<Repo> result = webClient.get().uri(GH_REPOS, login)
                    .retrieve().bodyToFlux(Repo.class)
                    .collectList()
                    .block();

            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
