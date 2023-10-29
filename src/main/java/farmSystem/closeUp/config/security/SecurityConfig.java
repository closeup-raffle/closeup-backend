package farmSystem.closeUp.config.security;


import farmSystem.closeUp.config.CorsConfig;
import farmSystem.closeUp.config.jwt.JwtAuthenticationFilter;
import farmSystem.closeUp.config.jwt.JwtService;
import farmSystem.closeUp.config.oauth.CustomOAuth2UserService;
import farmSystem.closeUp.config.oauth.handler.OAuth2LoginFailureHandler;
import farmSystem.closeUp.config.oauth.handler.OAuth2LoginSuccessHandler;
import farmSystem.closeUp.config.redis.RedisUtils;
import farmSystem.closeUp.repository.UserRepository;
import farmSystem.closeUp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final RedisUtils redisUtils;
    private final UserService userService;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()) //csrf 비활성
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) //폼 로그인 비활성
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) //HTTP 기본인증 비활성
                .addFilter(corsConfig.corsFilter())

                // 시큐리티가 세션을 만들지도 사용하지도 않음.
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 특정 URL에 대한 권한 설정
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/main","/login-success","/token/reissue", "/sign-up/**", "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/creator/**").hasRole("CREATOR")
                    .anyRequest().authenticated()
                )

                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정

                                );
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // jwtAuthFilter가 먼저 실행됨.// UsernamePasswordAuthenticationFilter는 인증되지 않은 사용자의 경우 로그인 페이지로 리다이렉션하므로 jwt 토큰 먼저 검증후 인증객체를 컨텍스트에 넣어야함
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userRepository,redisUtils,userService);
        return jwtAuthenticationFilter;
    }
}
