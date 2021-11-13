package com.test.token;


import com.alibaba.fastjson.JSONObject;
import com.test.entity.SecurityUser;
import com.test.util.JsonUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

public class JWTAccessToken extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

        if(authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            user.setPassword("");
            defaultOAuth2AccessToken.getAdditionalInformation().put("user_info",
                    JSONObject.parseObject(JSONObject.toJSONString(user)));
        }
        return super.enhance(defaultOAuth2AccessToken , authentication);
    }


    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        OAuth2AccessToken oAuth2AccessToken = super.extractAccessToken(value,map);
        convertData(oAuth2AccessToken , oAuth2AccessToken.getAdditionalInformation());
        return super.extractAccessToken(value, map);
    }
    private void convertData(OAuth2AccessToken accessToken,  Map<String, ?> map) {
        accessToken.getAdditionalInformation().put("user_info",convertUserData(map.get("user_info")));

    }

    private SecurityUser convertUserData(Object map) {
        String json = JsonUtils.deserializer(map) ;
        SecurityUser user = JsonUtils.serializable(json , SecurityUser.class);
        return  user;
    }

}
