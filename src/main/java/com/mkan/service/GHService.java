package com.mkan.service;

import com.mkan.controller.dto.OwnerDTO;
import com.mkan.controller.dto.OwnerRepoBranchesDTO;
import com.mkan.exception.UserNotFoundException;
import com.mkan.model.Branch;
import com.mkan.model.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.mkan.controller.dto.mapper.RepoBranchesMapper.map;

@Slf4j
@Service
public class GHService {

    private final WebClient.Builder webClientbuilder;
    public GHService(WebClient.Builder webClientbuilder) {
        this.webClientbuilder = webClientbuilder;
    }

    public OwnerRepoBranchesDTO findOwnerReposAndBranches(OwnerDTO ownerDTO) {

        String ownerLogin = ownerDTO.login();

        WebClient webClient = webClientbuilder.build();

        List<Repo> repositories = webClient.get()
                .uri("/users/{login}/repos", ownerLogin)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.statusCode().equals(HttpStatus.NOT_FOUND) ?
                                clientResponse.bodyToMono(UserNotFoundException.class) :
                                clientResponse.createException()
                )
                .bodyToFlux(Repo.class)
                .filter(repo -> !repo.fork())
                .flatMap(repo -> getBranches(ownerLogin, repo.name())
                        .map(branches -> new Repo(repo.name(),null, branches)))
                .collectList()
                .block();

        assert repositories != null;
        return map(ownerDTO, repositories);
    }

    private Mono<List<Branch>> getBranches(String ownerLogin, String repoName) {

        WebClient webClient = webClientbuilder.build();

        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", ownerLogin, repoName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .collectList();
    }
}
