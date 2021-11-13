package com.test.emun;

import lombok.Getter;

@Getter
public enum Redisconstant {

    RESOURCE_ROLES_MAP("AUTH:RESOURCE_ROLES_MAP");
    private String value;

    Redisconstant(String value){
        this.value = value;
    }

}
