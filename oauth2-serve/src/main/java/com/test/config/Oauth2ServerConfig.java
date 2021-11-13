package com.test.config;

import com.alibaba.fastjson.JSONObject;
import com.test.component.JwtTokenEnhancer;
import com.test.properties.JwtProperties;
import com.test.token.JWTAccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("detailService")
    private UserDetailsService userDetails;

    @Autowired
    @Qualifier("myClientDetailsService")
    private ClientDetailsService clientDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
//       clients.inMemory()
//               .withClient("client-app")
//               .secret("123456")
//               .scopes("all")
//               .authorizedGrantTypes("password" , "refresh_token")
//               .accessTokenValiditySeconds(3600)    // 访问token的有效期
//               .refreshTokenValiditySeconds(86400)  // 刷新token的有效期
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {



        endpoints.userDetailsService(userDetails)
                .tokenServices(defaultTokenServices())
                .authenticationManager(authenticationManager)
        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()");
    }

    @Bean("jwtConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JWTAccessToken();
        converter.setKeyPair(keyPair());
        return  converter;
    }



    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public KeyPair keyPair() {
            return new KeyStoreKeyFactory(jwtProperties().getKeyPath() ,  // 获取 密钥文件的路径
                    jwtProperties().getSecret().toCharArray())          //  将密码转化为char数组
                    .getKeyPair(jwtProperties().getAlias());            // 获取别名
    }



    @Bean("myClientDetailsService")
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        //  使用数据库存储
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        // 设置编码
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public DefaultTokenServices defaultTokenServices() {
        log.debug("defaultTokenServices 开始执行");
        DefaultTokenServices defaultTokenServices =new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        log.debug(JSONObject.toJSONString(defaultTokenServices));
        //  令牌增强
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(accessTokenConverter());
        delegates.add(new JwtTokenEnhancer());

        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(delegates);
        defaultTokenServices.setTokenEnhancer(chain);

        defaultTokenServices.setAccessTokenValiditySeconds(7200);
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);
        return defaultTokenServices;

    }

}
