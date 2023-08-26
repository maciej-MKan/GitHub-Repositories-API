package com.mkan.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatusCode;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class ExceptionMessage {

    Integer status;
    String message;

}
