package com.test.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AuthConstant {
    AUTHORITY_PREFIX("ROLE_"),
    AUTHORITY_CLAIM_NAME("authorities");

    private String value;

    AuthConstant(String value){
        this.value = value;
    }

}
