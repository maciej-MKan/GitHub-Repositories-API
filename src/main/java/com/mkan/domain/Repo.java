package com.mkan.domain;

import lombok.*;

import java.util.List;

@With
@Value
@Builder
@EqualsAndHashCode(of = "name")
@ToString(of = {"name"})
public class Repo {

    String name;
    Boolean fork;
    List<Branch> Branches;
    String default_branch;

}
