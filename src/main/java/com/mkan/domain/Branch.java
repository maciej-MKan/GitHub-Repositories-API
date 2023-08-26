package com.mkan.domain;

import lombok.*;

import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(of = "name")
@ToString(of = {"name"})
public class Branch {
    String name;
    Commit commit;
}
