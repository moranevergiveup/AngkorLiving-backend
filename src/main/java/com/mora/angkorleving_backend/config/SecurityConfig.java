package com.mora.angkorleving_backend.config;
import com.mora.angkorleving_backend.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) // or cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // ✅ disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/floors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rentals/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/rates/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/profile/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/users/profile/**").permitAll()
//                                .requestMatchers(HttpMethod.POST ,"/api/users/profile/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/floors/**").hasRole("ADMIN")
                        .requestMatchers("/api/rooms/**").hasRole("ADMIN")
                        .requestMatchers("/api/rentals/**").hasRole("ADMIN")
                        .requestMatchers("/api/payments/**").hasRole("ADMIN")

                                .requestMatchers("/api/rates/**").hasRole("ADMIN")
                                .requestMatchers("/api/expenses/**").hasRole("ADMIN")

                                //.requestMatchers("/api/users/**/reset-password/**").hasRole("ADMIN")


//                        .requestMatchers("/api/users/profile/**").hasAnyRole("TENANT", "USER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ✅ new style
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Secure password hashing
    }
}
