package com.test.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2TokenDto {
    private String token;
    private String refreshToken;
    private String tokenHead;
    private int expiresIn;

}
