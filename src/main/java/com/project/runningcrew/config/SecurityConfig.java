package com.project.runningcrew.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.security.ResponseUtils;
import com.project.runningcrew.security.exceptionhandler.CustomAccessDeniedHandler;
import com.project.runningcrew.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.project.runningcrew.security.filter.JwtAuthenticationFilter;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.security.filter.JwtVerifyFilter;
import com.project.runningcrew.security.filter.LoggingFilter;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final ResponseUtils responseUtils;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private final String[] SWAGGER_URL = {
            "/swagger-*/**", "/v3/api-docs/**"
    };
    private final String[] GET_PERMIT_API_URL = {
            "/api/sido-areas",
            "/api/sido-areas/**",
            "/api/gu-areas",
            "/api/gu-areas/**",
            "/api/crews",
            "/api/crews/*",
            "/api/crews/gu-areas/**"
    };
    private final String[] POST_PERMIT_API_URL = {
            "/api/login",
            "/api/users",
            "/api/users/duplicate/**",
            "/api/login/oauth"
    };

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtVerifyFilter jwtVerifyFilter = new JwtVerifyFilter(
                userRepository, userRoleRepository, SECRET_KEY, responseUtils);

        http.csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(SWAGGER_URL).permitAll()
                .antMatchers(HttpMethod.GET, GET_PERMIT_API_URL).permitAll()
                .antMatchers(HttpMethod.POST, POST_PERMIT_API_URL).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .apply(new MyCustomDsl())
                .and()
                .addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                    objectMapper, authenticationManager, jwtProvider, responseUtils);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
            builder.addFilter(jwtAuthenticationFilter);
        }
    }

}
