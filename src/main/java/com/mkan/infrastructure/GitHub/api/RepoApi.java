package com.mkan.infrastructure.GitHub.api;

import com.mkan.business.dao.RepoDAO;
import com.mkan.domain.Repo;
import com.mkan.domain.exception.AccessForbiddenException;
import com.mkan.domain.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Slf4j
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
            log.info("Handle response from APi correctly for login [{}]", login);

            return Optional.ofNullable(result);
        } catch (WebClientResponseException e) {
            log.error("Handle response from APi incorrect for [{}] cause [{}]", login, e.getMessage());
            if (e.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                throw new AccessForbiddenException("Forbidden access to GH API");
            }
            throw new UserNotFoundException("User Not Found");
        } catch (Exception e) {
            log.error("Handle response from APi incorrect for [{}] cause [{}]", login, e.getMessage());
            return Optional.empty();
        }

    }
}
