package com.mkan.domain;

import lombok.*;

@Getter
@Builder
public final class Commit {
    String sha;
    String url;
}