package com.mkan.domain;

import lombok.*;

import java.util.Map;

@Getter
@Builder
public class Branch {
    String name;
    Commit commit;
}
