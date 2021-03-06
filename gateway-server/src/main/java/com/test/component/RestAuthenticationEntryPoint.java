package com.test.component;

import com.alibaba.fastjson.JSONObject;
import com.test.api.CommonResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {

        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON_VALUE);
        String body = JSONObject.toJSONString(CommonResult.unathorized(e.getMessage()));

        DataBuffer wrap = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(wrap));
    }
}
