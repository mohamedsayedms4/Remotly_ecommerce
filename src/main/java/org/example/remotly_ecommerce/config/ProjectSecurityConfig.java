package org.example.remotly_ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.Filter.*;
import org.example.remotly_ecommerce.exception.CustomAccessDeniedHandler;
import org.example.remotly_ecommerce.exception.CustomBasicAuthenticationEntryPoint;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@RequiredArgsConstructor
public class ProjectSecurityConfig {
    private final UserRepository userRepository;


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

                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:5500")); // origin واضح
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // السماح بكل الطرق المهمة
                    config.setAllowedHeaders(Arrays.asList("*")); // السماح بأي header
                    config.setExposedHeaders(Arrays.asList("Authorization")); // إظهار Authorization للمتصفح
                    config.setAllowCredentials(true); // السماح بالكوكيز و Authorization header
                    config.setMaxAge(3600L); // مدة صلاحية preflight
                    return config;
                }))




                .csrf(csrfConfig -> csrfConfig.disable())
//                .csrf(csrfConfig -> csrfConfig
//                        .ignoringRequestMatchers("/auth/login")
//                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter() , BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter() , BasicAuthenticationFilter.class)



                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .authorizeHttpRequests(requests -> requests

//                        .requestMatchers("/sellers/profile").hasRole("ADMIN")
                                .requestMatchers("/csrf-token").permitAll()
                        .requestMatchers("/sellers/signup","sellers/login","/sellers/search","/products/seller/**","/products/id","/products/search","/products/category/**","/categories/**").permitAll()
                        .requestMatchers("/api/**","/myAccount","/sellers/**","products/insert","/users/**").authenticated()
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
