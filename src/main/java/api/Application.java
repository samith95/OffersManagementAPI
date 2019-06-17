package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application containing main function to run API
 * 
 * @author Samith Silva
 *
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories("api.repository")
public class Application extends SpringBootServletInitializer{
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
}