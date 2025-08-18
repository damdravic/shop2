package ro.pyc22.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.pyc22.shop.config.filters.JwtFilter;
import ro.pyc22.shop.exceptions.CustomAccessDeniedHandler;
import ro.pyc22.shop.exceptions.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {"/shop/**","/admin/register","/admin/login/**","/admin/verify/code/**","/admin/refresh/token/**"};
    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
                http .csrf(AbstractHttpConfigurer::disable);
                http.cors(AbstractHttpConfigurer::disable);
                http.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll());
                http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE,"/user/delete/**").hasAuthority("DELETE:USER"));
                http.authorizeHttpRequests(ar -> ar.anyRequest().authenticated());


        http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }



}
