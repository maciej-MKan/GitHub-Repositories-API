package com.mkan.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "name")
@ToString(of = {"name"})
public class Repo {

    String name;
    Boolean fork;

}
