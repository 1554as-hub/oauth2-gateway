package com.test.filter;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Component
@Slf4j
public class AuthGlobalFiler implements GlobalFilter , Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(token == null || token.isEmpty() || !(token.startsWith("Bearer ")|| token.startsWith("bearer "))){
            log.info(token);
            log.info("未进行授权！");
            return chain.filter(exchange);
        }
        String realToke = token.replace("Bearer " ,"");
        try {
            // 从token中解析用户信息并设置到Header中去
            JWSObject jwsObject = JWSObject.parse(realToke);
            String userStr = jwsObject.getPayload().toString();

            log.info("AuthGlobalFilter.filter() user:{}" , userStr);
            ServerHttpRequest user = null;

            user = exchange.getRequest().mutate().header("user", userStr).build();

            log.info(JSONObject.toJSONString(user));
            return chain.filter(exchange.mutate().request(user).build());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
