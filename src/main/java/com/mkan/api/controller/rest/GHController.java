package com.mkan.api.controller.rest;

import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.business.GHService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(GHController.API_PATH)
public class GHController {
    public static final String API_PATH = "/api";

    private final GHService ghService;

    @PostMapping(value = "/")
    public ResponseEntity<OwnerRepoBranchesDTO> reposAndBranches(@RequestBody @Valid OwnerDTO ownerDTO) {
        log.info("Handled [{}] in body", ownerDTO);

        OwnerRepoBranchesDTO response = ghService.findOwnerReposAndBranches(ownerDTO);
        log.info("Found [{}] for [{}]", response, ownerDTO);

        return ResponseEntity.ok(response);
    }
}
