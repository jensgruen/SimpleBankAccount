package com.example.bankAccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  private final PasswordEncoder passwordEncoder;

  public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public AuthenticationProvider authenticationProvider () {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

    authenticationProvider.setUserDetailsService(userDetailsService);
   authenticationProvider.setPasswordEncoder(passwordEncoder);

    return authenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers("/signup", "/login").permitAll()
            .anyRequest().authenticated()
    ).formLogin(
        formLogin -> formLogin.loginPage("/login")
            .defaultSuccessUrl("/home")
            .permitAll()
    );
    return http.build();

  }

}
