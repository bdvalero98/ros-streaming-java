package com.rosstreaming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "routeros")
public class RouterOSProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String interfaceName;
}
