package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity; consider enabling in production
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/", "/register", "/calculate", "/styles/**", "/css/**", "/js/**").permitAll() // Allow login and registration without authentication
                .anyRequest().authenticated() // Require authentication for all other requests
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/dashboard", true)
                .successHandler((request, response, authentication) -> { // Return 200 on success
                    response.setStatus(HttpServletResponse.SC_OK); 
                })
                .failureHandler((request, response, exception) -> { // Return 401 on failure
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                }) 
                .permitAll() // Configure form login
            );
        return http.build();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}