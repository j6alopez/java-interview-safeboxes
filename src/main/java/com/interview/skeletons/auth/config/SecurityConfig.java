package com.interview.skeletons.auth.config;

import com.interview.skeletons.config.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(TestCredentials.class)
@Profile({"dev", "test"})
public class SecurityConfig {
    private final TestCredentials testCredentials;

    private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    final String BASE_URL_MATCHER = ApiConfig.BASE_PATH + "/**";

    public SecurityConfig(TestCredentials testCredentials) {
        this.testCredentials = testCredentials;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        final UserDetails user = User.withUsername(testCredentials.user())
            .password(testCredentials.password())
            .authorities(testCredentials.role())
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(basic -> basic
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            )
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(BASE_URL_MATCHER).authenticated()
                .anyRequest().denyAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).build();
    }
}
