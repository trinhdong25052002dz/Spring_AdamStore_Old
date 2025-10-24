package Spring_AdamStore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomJwtDecoder customJwtDecoder;
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    private final String[] PUBLIC_URLS  = {
            "/v1/auth/login","/v1/auth/register", "/v1/auth/register/*", "/v1/auth/logout",
            "/v1/auth/refresh-token", "/v1/auth/forgot-password", "/v1/auth/forgot-password/*"
    };

    private final String[] GET_URLS  = {
            "/v1/branches", "/v1/branches/*",
            "/v1/products", "/v1/products/*",
            "/v1/categories", "/v1/categories/*",
            "/v1/colors", "/v1/colors/*",
            "/v1/sizes", "/v1/sizes/*"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_URLS).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(
                                        jwtConfigurer -> jwtConfigurer
                                                .decoder(customJwtDecoder)
                                                .jwtAuthenticationConverter(customJwtAuthenticationConverter)
                                )
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // cho phép yêu cầu từ cac cong
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:5173",
                "http://localhost:5174",
                "http://222.255.119.40:8080",
                "https://222.255.119.40"
        ));

        corsConfiguration.addAllowedMethod("*"); // cho phép tất cả method
        corsConfiguration.addAllowedHeader("*"); // cho phép tất cả header
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration); // dang ki cau hinh

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    // thiet lap url tren giao dien browser
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web ->
                web.ignoring().requestMatchers("/actuator/**","/v3/**", "webjar/**",
                        "/swagger-ui*/*swagger-initializer.js","/swagger-ui*/**");
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder(10);
    }
}
