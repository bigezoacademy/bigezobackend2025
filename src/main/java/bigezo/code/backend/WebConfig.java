package bigezo.code.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/**")
                .allowedOrigins("http://localhost:4200")  // Your frontend URL
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);  // Allow cookies or credentials (such as tokens)

        registry.addMapping("/api/requirements/**")
                .allowedOrigins("http://localhost:4200")  // Your frontend URL
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);  // Allow cookies or credentials
    }
}
