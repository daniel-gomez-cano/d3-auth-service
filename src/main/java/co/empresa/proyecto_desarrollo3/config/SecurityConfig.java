package co.empresa.proyecto_desarrollo3.config;

import co.empresa.proyecto_desarrollo3.security.JwtAuthConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ── Públicos ─────────────────────────────────────────────
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/test/public").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        .requestMatchers("/actuator/**").permitAll()

                        // ── Protegidos por rol ────────────────────────────────────
                        // Nota: @PreAuthorize en cada método complementa esta capa
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/creator/**").hasRole("ORGANIZER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Todo lo demás requiere estar autenticado
                        .anyRequest().authenticated()
                )

                // Necesario para H2 console (solo desarrollo)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // JWT validado contra Keycloak
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(new JwtAuthConverter())
                        )
                );

        return http.build();
    }

    /**
     * CORS abierto para desarrollo.
     * En producción, reemplaza List.of("*") por los dominios reales:
     *   config.setAllowedOrigins(List.of("https://tuapp.com", "https://admin.tuapp.com"));
     *
     * Otros microservicios que corran en Docker también necesitan estar en esta lista
     * si hacen llamadas HTTP directas al auth-service desde el navegador.
     * Las llamadas server-to-server (entre microservicios dentro de Docker) no
     * están sujetas a CORS — solo las del navegador.
     *
     * TODO: Agregar la lista de los demas servicios
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
