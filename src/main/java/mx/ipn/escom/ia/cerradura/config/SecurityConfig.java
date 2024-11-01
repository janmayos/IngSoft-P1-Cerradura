package mx.ipn.escom.ia.cerradura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si no es necesario
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/formlogin", "/formregister","/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/**").permitAll() // Permitir acceso a /auth sin autenticación
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Rutas solo para administradores
                        .requestMatchers("/user/**").hasRole("USER") // Rutas solo para usuarios
                        .anyRequest().authenticated())
                
                .formLogin(login -> login.loginPage("/formlogin")
                        .defaultSuccessUrl("/home", true) // Página a la que se redirige tras login exitoso
                        .permitAll())
                .logout(logout -> logout.permitAll()); // Permitir logout para todos los usuarios

        return http.build();
    }

    

    
}