package com.example.gamefiesta;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;







@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfiguration  {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http.csrf(AbstractHttpConfigurer::disable)
    //             .authorizeHttpRequests(request -> request.requestMatchers("/auth/**","/**")
    //                     .permitAll().anyRequest().authenticated())
    //             .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS ))
    //             .authenticationProvider(authenticationProvider).addFilterBefore(
    //                     jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    //     return http.build();
    // }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf(csrf -> csrf.disable())
          
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(auth -> 
            auth.requestMatchers("/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
          );
      
   // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
      http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));
      
      http.authenticationProvider(authenticationProvider);
  
      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
      
      return http.build();
    }


}