package frogslayer.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.repository.MemberRepository;
import frogslayer.auth.security.filter.CustomJsonAuthenticationFilter;
import frogslayer.auth.security.filter.JwtAuthenticationFilter;
import frogslayer.auth.security.repository.RefreshTokenRepository;
import frogslayer.auth.security.service.JwtService;
import frogslayer.auth.security.service.oauth2.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();

        //corsConfig.addAllowedOrigin("");
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityFilterChain httpFilterChain (HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors((cors) ->
                        cors.configurationSource(configurationSource()))
                .csrf((csrf) ->
                        csrf.disable())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .requiresChannel((requiresChannel) -> requiresChannel
                        .requestMatchers("/api/oauth2/authorization").requiresSecure()
                )
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .anyRequest().authenticated()
                )
                .oauth2Login((oauth2login) ->
                        oauth2login
                                .loginPage("/login")
                                //.successHandler(oAuth2LoginSuccessHandler)
                                //.failureHandler(oAuth2LoginFailureHandler)
                                .authorizationEndpoint((endpoint) -> endpoint
                                        .baseUri("/api/oauth2/authorization"))
                                .redirectionEndpoint((endpoint) ->
                                        endpoint.baseUri("/api/login/oauth2/code/*"))
                                //.userInfoEndpoint((endpoint) ->
                                //        endpoint.userService(customOAuth2UserService))
                )
                .logout((logout) ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    return;
                                })
                                .invalidateHttpSession(true)
                                .deleteCookies("refreshToken")
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                response.setStatus(403);
                            }
                        })
                );

        http.addFilterAfter(jsonAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), CustomJsonAuthenticationFilter.class);
        return http.build();
    }

    public CustomJsonAuthenticationFilter jsonAuthenticationFilter(){
        return new CustomJsonAuthenticationFilter(new ObjectMapper());
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtService, memberRepository, refreshTokenRepository);
    }

}
