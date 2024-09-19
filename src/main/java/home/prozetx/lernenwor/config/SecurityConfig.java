package home.prozetx.lernenwor.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationAccessTokenFilter jwtAuthenticationAccessTokenFilter;
    private final JwtAuthenticationRefreshTokenFilter jwtAuthenticationRefreshTokenFilter;
    public static final String AUTH_PATTERN = "/v1/auth/";
    public static final String UPDATE_TOKENS_ENDPOINT = "/api/v1/auth/updateTokens";
    public static final String ALL_USERS_ENDPOINT = "/v1/users";
    public static final String CHECK_EXISTING_USER_DATA_PATTERN = "/v1/users/checks/";
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        //.anyRequest().permitAll() //authenticated()
                        .requestMatchers(HttpMethod.POST, AUTH_PATTERN + "**").permitAll()
                        .requestMatchers(HttpMethod.GET, UPDATE_TOKENS_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.GET, ALL_USERS_ENDPOINT, CHECK_EXISTING_USER_DATA_PATTERN + "**").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationAccessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationRefreshTokenFilter, JwtAuthenticationAccessTokenFilter.class);
//                .httpBasic(Customizer.withDefaults())
//                .userDetailsService(userDetailsService());
//                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
