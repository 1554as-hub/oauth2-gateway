package com.test.config;

import com.test.component.AuthorizationManager;
import com.test.component.RestAuthenticationEntryPoint;
import com.test.component.RestfulAccessDeniedHandler;
import com.test.domain.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class ResourceServerConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private RestAuthenticationEntryPoint entryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        String[] writeList = new String[ignoreUrlsConfig.getUrls().size()];
        for (int i = 0 ; i < ignoreUrlsConfig.getUrls().size() ; i++){
            writeList[i] = ignoreUrlsConfig.getUrls().get(i);
        }

        log.debug("SecurityWebFilterChain开始执行！");

        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAccessTokenConverter());
        //  自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(entryPoint);

        http.authorizeExchange()
                .pathMatchers(writeList).permitAll()                    //  白名单配置
                .anyExchange().access(authorizationManager)             // 鉴权管理器配置
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)        // 处理未授权
                .authenticationEntryPoint(entryPoint)                   // 处理未认证
                .and()
                .csrf().disable();
        return http.build();
    }



    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAccessTokenConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX.getValue());
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME.getValue());
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }



}
