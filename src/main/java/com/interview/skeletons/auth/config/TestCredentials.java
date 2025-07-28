package com.interview.skeletons.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credentials")
public record TestCredentials(
    String user,
    String password,
    String role
) {
}
