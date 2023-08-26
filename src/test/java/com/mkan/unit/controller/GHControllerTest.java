package com.mkan.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkan.api.controller.rest.GHController;
import com.mkan.api.dto.BranchDTO;
import com.mkan.api.dto.OwnerDTO;
import com.mkan.api.dto.OwnerRepoBranchesDTO;
import com.mkan.api.dto.RepoDTO;
import com.mkan.business.GHService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GHController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GHControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private GHService ghService;

    private static final String login = "test";
    private static final OwnerDTO someOwner = OwnerDTO.builder().login(login).build();
    private static final OwnerRepoBranchesDTO someRepoBranches = OwnerRepoBranchesDTO.builder()
            .login(login)
            .repositories(List.of(
                    RepoDTO.builder()
                            .branches(List.of(BranchDTO.builder().build()))
                            .build()
            ))
        .build();
    @Test
    void thatGHControllerWorkCorrectly() throws Exception {

        //given
        String requestBody = objectMapper.writeValueAsString(someOwner);
        String responseBody = objectMapper.writeValueAsString(someRepoBranches);

        when(ghService.findOwnerReposAndBranches(any())).thenReturn(someRepoBranches);

        // when, then
        MvcResult result = mockMvc.perform(get(
                        GHController.API_PATH + "/"
                )
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(login)))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(responseBody);
    }

    @Test
    void thatGHControllerShouldThrowValidationException() throws Exception {

        //given
        String requestBody = objectMapper.writeValueAsString(OwnerDTO.builder().build());

        when(ghService.findOwnerReposAndBranches(any())).thenReturn(someRepoBranches);

        // when, then
        MvcResult result = mockMvc.perform(get(
                        GHController.API_PATH + "/"
                )
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
