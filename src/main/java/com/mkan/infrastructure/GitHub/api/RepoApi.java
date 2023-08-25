package com.mkan.infrastructure.GitHub.api;

import com.mkan.business.dao.RepoDAO;
import com.mkan.domain.Owner;
import com.mkan.domain.Repo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RepoApi implements RepoDAO {

    private static final String GH_REPOS = "users/{owner_login}/repos";
    private final WebClient webClient;

    @Override
    public Optional<Repo> getRepoByOwner(Owner owner){
        try {
            Repo result = webClient
                    .get()
                    .uri(GH_REPOS, owner.getLogin())
                    .retrieve()
                    .bodyToMono(Repo.class)
                    .block();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
