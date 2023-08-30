package com.mkan.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;


public record OwnerDTO (@Valid @NotEmpty String login){}
