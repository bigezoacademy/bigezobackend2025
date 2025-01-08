package bigezo.code.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/**")
                .allowedOrigins("*") // Frontend URL
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
        registry.addMapping("/api/**")
                .allowedOrigins("*") // Frontend URL
                .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
