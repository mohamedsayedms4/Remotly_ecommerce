package org.example.remotly_ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.remotly_ecommerce.Filter.*;
import org.example.remotly_ecommerce.exception.CustomAccessDeniedHandler;
import org.example.remotly_ecommerce.exception.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * This configuration class defines the security setup for the application.
 * It configures the HTTP security rules, creates an in-memory user, and sets up password encoding.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ProjectSecurityConfig {


    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    /**
     * Defines the security filter chain, specifying authorization rules for HTTP requests.

     * - Paths "/myAccount", "/myBalance", "/myLoans", "/myCards" require authentication.
     * - Paths "/notices", "/contact", "/error" are publicly accessible.
     * - Disables form-based login.
     * - Enables HTTP Basic authentication.
     *
     * @param http HttpSecurity object to configure HTTP security.
     * @return the configured SecurityFilterChain bean.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    @Order
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:5500"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))



                .csrf(csrfConfig -> csrfConfig.disable())
//                .csrf(csrfConfig -> csrfConfig
//                        .ignoringRequestMatchers("/auth/signup","/auth/login","/auth/loginWithOtp","/auth/verifyOtp")
//                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter() , BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter() , BasicAuthenticationFilter.class)



                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .authorizeHttpRequests(requests -> requests

//                        .requestMatchers("/sellers/profile").hasRole("ADMIN")
                        .requestMatchers("/sellers/signup","sellers/login","/sellers/search","/products/seller/**","/products/id","/products/search","/products/category/**","/categories/**").permitAll()
                        .requestMatchers("/api/**","/myAccount","/sellers/**","products/insert").authenticated()
                        .requestMatchers("/auth/signup", "/auth/login", "/auth/loginWithOtp", "/auth/verifyOtp").permitAll()

        );
        // Disable default Spring Security form login page
        http.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable());
        // Enable HTTP Basic authentication
        http.exceptionHandling(exceptions ->exceptions.accessDeniedHandler(new CustomAccessDeniedHandler()));

        http.httpBasic(httpSecurityHttpBasicConfigurer ->
                httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        return http.build();
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        RemotlyUserNamePasswordAuthenticationProvider authenticationProvider =
                new RemotlyUserNamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }
//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }
}
