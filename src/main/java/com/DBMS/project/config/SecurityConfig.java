package com.DBMS.project.config;

import com.DBMS.project.service.UserInfoService;
 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.AuthenticationProvider; 
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.web.SecurityFilterChain; 

  
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Deprecated
public class SecurityConfig { 

  
    // User Creation 
    @Bean
    public UserDetailsService userDetailsService() { 
        return new UserInfoService(); 
    } 
  
    // Configuring HttpSecurity 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests()
                .requestMatchers("/login", "/login/**", "/register/client", "/products/**", "/courses/**", "/blogs/**","/error", "/notfound","/styles/**","/images/**").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/clients/**","/create-checkout-session","/success/**").hasAuthority("ROLE_CLIENT")
                .and()
                .authorizeHttpRequests().requestMatchers("/offers/**","/admin/**").hasAuthority("ROLE_ADMIN")
                .and()
                .authorizeHttpRequests().requestMatchers("/").authenticated()
                .and()
                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/").permitAll())
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .build();
    } 
  
    // Password Encoding 
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 

    
  
    @Bean
    public AuthenticationProvider authenticationProvider() { 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsService()); 
        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
        return authenticationProvider; 
    } 
  
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 
    
  
  
}