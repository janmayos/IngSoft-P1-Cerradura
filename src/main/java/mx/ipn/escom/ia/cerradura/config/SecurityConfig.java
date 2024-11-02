package mx.ipn.escom.ia.cerradura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;


// @Configuration
// @EnableWebSecurity
public class SecurityConfig {

    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEwncoder();
    // }
    
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //     .csrf(csrf -> csrf
    //         .disable() // Deshabilitar CSRF si no es necesario
    //         ) // Ignorar CSRF para las rutas de /auth
    
    //     .authorizeHttpRequests(requests -> requests
    //         .requestMatchers("/formlogin", "/formregister", "/PaginaInicio", "/css/**", "/js/**", "/images/**").permitAll()
    //         .requestMatchers("/auth/**").permitAll() // Permitir acceso a /auth sin autenticación
    //         .requestMatchers("/api/**").permitAll()
    //         .requestMatchers("/admin/**").hasRole("ADMIN") // Rutas solo para administradores
    //         .requestMatchers("/user/**").hasRole("USER") // Rutas solo para usuarios
    //         .anyRequest().permitAll()) // Rutas restantes permitidas
    
    //     .formLogin(login -> login
    //         .loginPage("/formlogin")
    //         .defaultSuccessUrl("/PaginaInicio", true) // Página a la que se redirige tras login exitoso
    //         .permitAll())
        
    //     .logout(logout -> logout
    //         .permitAll()) // Permitir logout para todos los usuarios
        
    //     // Configuración de la gestión de sesiones
    //     .sessionManagement(session -> session
    //         .sessionFixation().newSession() // Crear una nueva sesión después del login
    //         .maximumSessions(1) // Límite de una sesión por usuario
    //         .expiredUrl("/formlogin?expired=true") // Redirigir al login si la sesión expira
    //     );
    
    

    //     return http.build();
    // }

    

    
}