package mx.ipn.escom.ia.cerradura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import lombok.RequiredArgsConstructor;
import mx.ipn.escom.ia.cerradura.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authProvider;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/formlogin", "/formregister", "/css/**",
                                                                "/js/**", "/images/**", "/antes/**")
                                                .permitAll()
                                                .requestMatchers("/PaginaInicio").permitAll()
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/vista/usuarios/**").permitAll()
                                                .requestMatchers("/resultadosLibros").permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exception -> exception
                                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
                                

                return http.build();
        }
}
