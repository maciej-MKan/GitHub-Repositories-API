package com.mkan.api.dto;

import lombok.*;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor(staticName = "of")
public class ExceptionMessage {

    Integer status;
    String message;

}
