package com.uriel.travel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uriel.travel.jwt.JwtAuthenticationFilter;
import com.uriel.travel.jwt.JwtTokenProvider;
import com.uriel.travel.jwt.JwtVerificationFilter;
import com.uriel.travel.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final CorsConfig corsConfig;
//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilter(corsConfig.corsFilter())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());
//                .oauth2Login((oauth2) -> oauth2
//                        .successHandler(oAuth2LoginSuccessHandler)
//                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
//                                .userService(customOAuth2UserService)));

                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
        return http.build();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {

            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider, redisService, new ObjectMapper());
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(redisService, jwtTokenProvider);

            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
//            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
//            jwtAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailurHandler());

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}