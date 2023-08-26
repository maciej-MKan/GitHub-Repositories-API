package com.mkan.domain;

import lombok.*;

@Value
@Builder
@EqualsAndHashCode(of = "sha")
@ToString(of = {"sha"})
public class Commit {
    String sha;
    String url;
}