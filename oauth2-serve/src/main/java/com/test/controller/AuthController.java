package com.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.test.api.CommonResult;
import com.test.domain.Oauth2TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@Slf4j
public class AuthController {


    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping("/oauth/token")
    public CommonResult<Oauth2TokenDto> postAccessToken(Principal principal , @RequestParam
    Map<String , String> parameters) throws HttpRequestMethodNotSupportedException {

        log.info("postAccessToken 开始执行！");
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();


        return  CommonResult.success(new Oauth2TokenDto(oAuth2AccessToken.getValue() ,
                "" ,
                "Bearer" ,
                oAuth2AccessToken.getExpiresIn())
        );

    }

}
