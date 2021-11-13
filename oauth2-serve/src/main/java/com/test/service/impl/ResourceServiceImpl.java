package com.test.service.impl;

import com.test.emun.Redisconstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ResourceServiceImpl {

    @Autowired
    private RedisTemplate<String , Object> redisTemplate;


    @PostConstruct
    public void initData() {

        Map<String , List<String>> resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/api-website/hello" , Collections.singletonList("ADMIN"));
        resourceRolesMap.put("/api-website/echo/**" , Collections.singletonList("ADMIN"));
        resourceRolesMap.put("/api-work/echo/**" , Collections.singletonList("ADMIN"));
        resourceRolesMap.put("/api-website/user/currentUser" , Arrays.asList("ADMIN" , "USER"));
        //  判断是否存在Key值 存在则删除
        if(redisTemplate.hasKey(Redisconstant.RESOURCE_ROLES_MAP.getValue())){
            redisTemplate.delete(Redisconstant.RESOURCE_ROLES_MAP.getValue());
        }

        redisTemplate.opsForHash().putAll(Redisconstant.RESOURCE_ROLES_MAP.getValue() , resourceRolesMap);
    }


}
