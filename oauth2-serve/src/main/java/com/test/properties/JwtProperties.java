package com.test.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("keytool.auth.server")
public class JwtProperties implements Serializable {

    private Integer maxClient;

    private Integer tokenValid;

    private Boolean force;

    private Boolean startRefresh;

    private Integer refreshTokenValid;

    private Resource keyPath;

    private String alias;

    private String secret;

}
