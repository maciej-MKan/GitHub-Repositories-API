package com.mkan.integration.support;

import com.mkan.api.controller.rest.GHController;
import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

public interface GHControllerTestSupport {

    RequestSpecification requestSpecification();
    default OwnerRepoBranchesDTO getUsersReposAndBranches(final OwnerDTO ownerDTO){
        return requestSpecification()
                .body(ownerDTO)
                .get(GHController.API_PATH + "/")
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(OwnerRepoBranchesDTO.class);
    }
}
