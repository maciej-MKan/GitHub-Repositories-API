package com.mkan.integration.support;

import com.mkan.api.controller.rest.GHController;
import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

public interface GHControllerTestSupport {

    RequestSpecification requestSpecification();
    default OwnerRepoBranchesDTO getUsersReposAndBranchesCorrectly(final OwnerDTO ownerDTO){
        return requestSpecification()
                .body(ownerDTO)
                .accept("application/json")
                .post(GHController.API_PATH + "/")
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(OwnerRepoBranchesDTO.class);
    }

    default ValidatableResponse getWithIncorrectAccept(final OwnerDTO ownerDTO){
        return requestSpecification()
                .body(ownerDTO)
                .accept("application/xml")
                .post(GHController.API_PATH + "/")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .contentType(ContentType.JSON)
                .and();
    }

    default ValidatableResponse getWithIncorrectLogin(final OwnerDTO ownerDTO){
        return requestSpecification()
                .body(ownerDTO)
                .accept("application/json")
                .post(GHController.API_PATH + "/")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .and();
    }

    default ValidatableResponse getWithAccessForbidden(final OwnerDTO ownerDTO){
        return requestSpecification()
                .body(ownerDTO)
                .accept("application/json")
                .post(GHController.API_PATH + "/")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .contentType(ContentType.JSON)
                .and();
    }

}
