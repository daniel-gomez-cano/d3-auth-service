package co.empresa.proyecto_desarrollo3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import co.empresa.proyecto_desarrollo3.security.JwtAuthConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // públicos
                .requestMatchers("/").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()

                // protegidos por rol
                .requestMatchers("/client/**").hasRole("CLIENT")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/creator/**").hasRole("EVENT_CREATOR")

                .anyRequest().authenticated()
            )
            
            // necesario para H2
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // JWT (Keycloak)
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(new JwtAuthConverter())
                )
            );

        return http.build();
    }
}