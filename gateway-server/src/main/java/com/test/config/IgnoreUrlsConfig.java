package com.test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "secure.ignore")
@Getter
@Setter
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
