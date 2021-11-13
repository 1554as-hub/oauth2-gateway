package com.test.domain.holder;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.test.domain.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class LoginuserHolder {

    @Autowired
    private RestTemplate restTemplate;

    private final String CHECK_TOKE_URI = "http://OAUTH2-SERVE/auth/oauth/check_token";

    public UserDto getCurrentUser()  {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String user = request.getHeader("user");

        JSONObject userObject = JSONObject.parseObject(user);
        log.debug(userObject.toString());


        JSONObject jsonObject = JSONObject.parseObject(userObject.getString("user_info"));
        log.debug(jsonObject.toString());


        UserDto userDto = new UserDto();
        userDto.setId(jsonObject.getString("id"));
        userDto.setUsername(jsonObject.getString("username"));
        userDto.setCreateTime(jsonObject.getString("createTime"));
        userDto.setBirthday(jsonObject.getString("birthday"));
        try {
            userDto.setRealname(new String(jsonObject.getString("realname").getBytes() , "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userDto.setSex(jsonObject.getString("sex"));
        userDto.setPhone(jsonObject.getString("phone"));
        userDto.setAvatar(jsonObject.getString("avatar"));
        userDto.setAuthorities( Convert.toList(String.class ,userObject.getString("authorities")));
        return  userDto;
    }

}
