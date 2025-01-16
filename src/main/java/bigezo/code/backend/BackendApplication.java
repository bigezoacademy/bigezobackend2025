package bigezo.code.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "bigezo.code.backend")
public class BackendApplication {

	public static void main(String[] args) {
		// Load the .env file
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // Directory containing the .env file
				.filename(".env") // Name of the .env file
				.load();

		// Set environment variables as system properties (if needed)
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_JWT_SECRET", dotenv.get("SPRING_JWT_SECRET"));
		System.setProperty("SPRING_JWT_EXPIRATION", dotenv.get("SPRING_JWT_EXPIRATION"));

		SpringApplication.run(BackendApplication.class, args);
	}
}
