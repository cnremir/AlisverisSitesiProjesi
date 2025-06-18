package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@Table(name = "Role")
public enum Role implements GrantedAuthority {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");
    private String value;
    Role(String value){
        this.value = value;
    }


    public String getValue(){
        return this.value;
    }


    @Override
    public String getAuthority() {

        return "ROLE_" + this.value;
    }
}
