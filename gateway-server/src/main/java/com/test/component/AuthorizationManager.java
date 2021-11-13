package com.test.component;

import cn.hutool.core.convert.Convert;
import com.test.domain.AuthConstant;
import com.test.domain.Redisconstant;
import com.test.util.UrlConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.PathMatcher;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate<String , Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();

        if(request.getMethod() == HttpMethod.OPTIONS){
            return Mono.just(new AuthorizationDecision(true));
        }

        List<String> authorities = new ArrayList<>();
        Map<Object , Object> resourceRolesMap = redisTemplate.opsForHash().entries(Redisconstant.RESOURCE_ROLES_MAP.getValue());

        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();

        while (iterator.hasNext()){
            //  生成正则表达式匹配项
            String pattern = (String) iterator.next();
            String regPath = UrlConvertUtil.getRegPath(pattern);

            //  查看是否符合匹配项  符合就通过不符合则继续
            if(request.getURI().getPath().matches(regPath)){
                Object roles = redisTemplate
                        .opsForHash().get(Redisconstant.RESOURCE_ROLES_MAP.getValue(), pattern);
                authorities = Convert.toList(String.class , roles);
                log.debug("匹配成功！" + authorities);
                break;
            }
        }

         authorities = authorities.stream().map(i -> AuthConstant.AUTHORITY_PREFIX.getValue() + i).collect(Collectors.toList());
        log.debug(authorities.toString());

        List<String> finalAuthorities = authorities;
        return mono.filter(a -> a.isAuthenticated())
                .flatMapIterable(a -> a.getAuthorities())
                .map(g -> g.getAuthority())
                .any(c -> {
                    String[] roles = c.split(",");
                    log.debug(Arrays.toString(roles));
                    for (String role : roles){
                        if(finalAuthorities.contains(role)){
                            log.debug("匹配成功Roles 为" + role);
                            return true;
                        }

                    }
                    return false;
                })
                .map(hasAuthority -> new AuthorizationDecision(hasAuthority))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
