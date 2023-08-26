package com.mkan.domain;

import lombok.*;

@Value
@Builder
@EqualsAndHashCode(of = "login")
@ToString(of = {"login"})
public class Owner {

    String login;

}
