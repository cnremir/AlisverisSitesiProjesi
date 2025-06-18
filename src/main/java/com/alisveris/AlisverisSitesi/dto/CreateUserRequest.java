package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
public record CreateUserRequest(
        String name,
        String surname,
        String username,
        String password,
        Set<Role> authorities,
        String userMailAdress,
        Double balance


) {


}
