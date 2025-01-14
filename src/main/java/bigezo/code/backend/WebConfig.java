package bigezo.code.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Authentication and requirements mappings
        registry.addMapping("/auth/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/api/requirements/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Add CORS for student APIs
        registry.addMapping("/api/students/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
