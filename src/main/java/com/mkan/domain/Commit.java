package com.mkan.domain;

import lombok.*;

@Getter
@Builder
public class Commit {
    String sha;
    String url;
}