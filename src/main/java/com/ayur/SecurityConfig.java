//SecurityConfig.java
package com.ayur;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use strength 10 for a good balance of security and speed
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF for APIs so your JS fetch() calls work
            .csrf(csrf -> csrf.disable())
            
            // 2. Configure Permissions
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/login", "/signup", 
                    "/api/auth/**", "/api/clients/**",
                    "/css/**", "/js/**", "/images/**", "/webjars/**"
                ).permitAll()
                .anyRequest().permitAll() // Allow all for now to debug startup
            )
            
            // 3. Disable default Form Login since you are using a custom LoginController + Fetch
            .formLogin(form -> form.disable())
            
            // 4. Disable Basic Auth popup in browsers
            .httpBasic(basic -> basic.disable())
            
            // 5. Allow H2 console or Frames if needed (optional)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}