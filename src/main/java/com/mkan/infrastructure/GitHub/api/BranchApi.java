package com.mkan.infrastructure.GitHub.api;

import com.mkan.business.dao.BranchDAO;
import com.mkan.domain.Branch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BranchApi
//        extends AbstractGHAPI
        implements BranchDAO {

    private static final String GH_BRANCHES = "repos/maciej-MKan/{repo_name}/branches";
    private final WebClient webClient;

//    public BranchApi(WebClient webClient) {
//        super(webClient);
//    }

    @Override
    public List<Branch> findBranchesByRepoName(String name, String mainBranchName) {
        return getBranchByRepoName(name, mainBranchName).orElseThrow();
    }

    private Optional<List<Branch>> getBranchByRepoName(String name, String mainBranchName) {
        try {
            List<Branch> result = Objects.requireNonNull(webClient.get().uri(GH_BRANCHES, name)
                            .retrieve()
                            .bodyToFlux(Branch.class)
                            .collectList()
                            .block())
                    .stream()
                    .filter(branch -> branch.getName().equals(mainBranchName))
                    .toList();

            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
