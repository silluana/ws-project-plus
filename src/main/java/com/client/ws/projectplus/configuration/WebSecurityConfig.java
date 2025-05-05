package com.client.ws.projectplus.configuration;

import com.client.ws.projectplus.filter.AuthenticationFilter;
import com.client.ws.projectplus.repository.UserDetailsRepository;
import com.client.ws.projectplus.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String[] AUTH_SWAGGER_LIST = {
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

    private TokenService tokenService;
    private UserDetailsRepository userDetailsRepository;

    public WebSecurityConfig(TokenService tokenService, UserDetailsRepository userDetailsRepository) {
        this.tokenService = tokenService;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
                web.ignoring()
                        .requestMatchers( AUTH_SWAGGER_LIST)
                        .requestMatchers(HttpMethod.GET, "/subscription-type")
                        .requestMatchers(HttpMethod.POST, "/user")
                        .requestMatchers(HttpMethod.POST, "/payment/process")
                        .requestMatchers(HttpMethod.POST, "/auth")
                        .requestMatchers( "/auth/recovery-code/*");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .addFilterBefore(new AuthenticationFilter(tokenService, userDetailsRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
