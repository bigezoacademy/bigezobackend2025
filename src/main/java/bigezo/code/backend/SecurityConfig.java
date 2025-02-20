package bigezo.code.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/auth/**", "/error", "/auth/student-login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Pesapal API endpoints (protected with API Key, not JWT)
                        .requestMatchers(HttpMethod.POST, "/api/pesapal/**").permitAll()  // Allow all requests to these endpoints
                        .requestMatchers(HttpMethod.GET, "/api/pesapal/payment-status/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sms/send").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/requirements").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/requirements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/requirements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/requirements/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/transactions/pesapal-status/**").permitAll()

                        // School Fees Settings API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/school-fees-settings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/school-fees-settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/school-fees-settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/school-fees-settings/**").hasRole("ADMIN")

                        // School Fees Details API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/school-fees-details/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/school-fees-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/school-fees-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/school-fees-details/**").hasRole("ADMIN")
                        // Student API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")

                        // Catch-all for authenticated requests
                        .anyRequest().authenticated()
                )
                // No JWT authentication filter for Pesapal endpoints, since we're using API key
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
