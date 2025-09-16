package threeoone.bigproject;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point of the application.
 * Enable Feign Clients for OpenFeign
 *
 * @author DUCBRICK
 */
@SpringBootApplication
@EnableFeignClients
public class BigprojectApplication {
	/**
	 * Launches a Spring Boot {@link JavaFxApplication}.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}
}
