package com.mkan.domain;

import lombok.*;

import java.util.Map;

@Getter
@Builder
public final class Branch {
    String name;
    Commit commit;
}
