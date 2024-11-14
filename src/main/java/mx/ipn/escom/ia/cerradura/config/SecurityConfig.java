package mx.ipn.escom.ia.cerradura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import mx.ipn.escom.ia.cerradura.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable()) // Configura CORS si es necesario en lugar de deshabilitarlo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/formlogin", "/formregister", "/css/**", "/js/**", "/images/**", "/antes/**","/Usuarios/**")
                        .permitAll()
                        .requestMatchers("/auth/**", "/api/check-connection", "/", "/formlogin",
                                "/PaginaInicio","/api/usuarios/**","favicon.ico","/vista/usuarios/**")
                        .permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desactivar sesiones

                ).authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
