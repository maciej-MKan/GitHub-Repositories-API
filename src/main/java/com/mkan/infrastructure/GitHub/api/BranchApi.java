package com.mkan.infrastructure.GitHub.api;

import com.mkan.business.dao.BranchDAO;
import com.mkan.domain.Branch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class BranchApi implements BranchDAO {

    private static final String GH_BRANCHES = "repos/maciej-MKan/{repo_name}/branches";
    private final WebClient webClient;


    @Override
    public List<Branch> findBranchesByRepoName(String name) {
        return getBranchByRepoName(name).orElse(List.of(Branch.builder().build()));
    }

    private Optional<List<Branch>> getBranchByRepoName(String name) {
        try {
            List<Branch> result = Objects.requireNonNull(webClient.get().uri(GH_BRANCHES, name)
                            .retrieve()
                            .bodyToFlux(Branch.class)
                            .collectList()
                            .block());
            log.info("Handle response from APi correctly for branch [{}]", name);

            return Optional.of(result);
        } catch (NoSuchElementException e) {
            log.error("Handle response from APi incorrect for [{}] cause [{}]", name, e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Handle response from APi incorrect for [{}] cause [{}]", name, e.getMessage());
            throw new NoSuchElementException(e);
        }

    }
}
