package com.mkan.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "name")
@ToString(of = {"name"})
public class Branch {

    String name;
    String sha;

}
