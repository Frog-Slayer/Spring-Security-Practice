package security.practice.global.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import security.practice.domain.member.repository.MemberRepository;
import security.practice.global.auth.filter.JsonUsernamePasswordAuthenticationFilter;
import security.practice.global.auth.filter.JwtAuthenticationFilter;
import security.practice.global.auth.filter.RefreshTokenAuthenticationFilter;
import security.practice.global.auth.handler.LoginSuccessHandler;
import security.practice.global.auth.provider.JwtAuthenticationProvider;
import security.practice.global.auth.provider.RefreshTokenAuthenticationProvider;
import security.practice.global.auth.service.CustomUserDetailsService;
import security.practice.global.auth.service.jwt.JwtService;
import security.practice.global.auth.token.RefreshTokenAuthenticationToken;
import security.practice.global.auth.utils.FilterSkipMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addExposedHeader("Authorization");
        corsConfig.addExposedHeader("Set-Cookie");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors ->
                  cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/member/register/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), JsonUsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(refreshTokenAuthenticationFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return delegatingPasswordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtService);
        jwtAuthenticationProvider.setUserDetailsService(userDetailsService());

        RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider = new RefreshTokenAuthenticationProvider(jwtService);
        refreshTokenAuthenticationProvider.setUserDetailsService(userDetailsService());

        return new ProviderManager(daoAuthenticationProvider, jwtAuthenticationProvider, refreshTokenAuthenticationProvider);
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(jwtService);
    }

    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter();

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());

        return filter;
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        List<String> skipList = new ArrayList<>();
        skipList.add("/login");
        skipList.add("/member/register");
        skipList.add("/refresh");

        FilterSkipMatcher skipMatcher = new FilterSkipMatcher(skipList);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipMatcher, jwtService);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    public RefreshTokenAuthenticationFilter refreshTokenAuthenticationFilter() throws Exception {
        RefreshTokenAuthenticationFilter filter = new RefreshTokenAuthenticationFilter(jwtService);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}