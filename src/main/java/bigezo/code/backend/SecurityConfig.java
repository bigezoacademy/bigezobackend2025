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

                        // Payment endpoints
                        .requestMatchers(HttpMethod.POST, "/api/payments").permitAll()  // Allow all POST requests to payments endpoint
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").permitAll()

                        // Pesapal API endpoints (protected with API Key, not JWT)
                        .requestMatchers(HttpMethod.POST, "/api/pesapal/**").permitAll()  // Allow all requests to these endpoints
                        .requestMatchers(HttpMethod.GET, "/api/pesapal/payment-status/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pesapal/transaction-status/**").permitAll() // Added line for transaction-status


                        // SMS API (Admin Only)
                        .requestMatchers(HttpMethod.POST, "/api/sms/send").hasRole("ADMIN")

                        // Requirements API (Admin Only)
                        .requestMatchers(HttpMethod.GET, "/api/requirements").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/requirements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/requirements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/requirements/**").hasRole("ADMIN")

                        // Transactions API
                        .requestMatchers(HttpMethod.GET, "/api/transactions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/transactions/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/transactions/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/transactions/pesapal-status/**").permitAll()

                        // School Fees Settings API (Admin Only)
                        .requestMatchers(HttpMethod.GET, "/api/school-fees-settings/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/school-fees-settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/school-fees-settings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/school-fees-settings/**").hasRole("ADMIN")

                        // School Fees Details API (Admin Only)
                        .requestMatchers(HttpMethod.GET, "/api/school-fees-details/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/school-fees-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/school-fees-details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/school-fees-details/**").hasRole("ADMIN")

                        // Students API (User or Admin Access)
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")

                        // Files API (Admin Only)
                        .requestMatchers(HttpMethod.POST, "/api/files/schools/{schoolId}/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/files/schools/{schoolId}/delete/{fileName}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/files/schools/{schoolId}/list").hasAnyRole("USER", "ADMIN")

                        // Catch-all for authenticated requests
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT Filter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
