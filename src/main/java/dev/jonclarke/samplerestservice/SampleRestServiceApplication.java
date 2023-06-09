package dev.jonclarke.samplerestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.jonclarke.samplerestservice.dataaccess.ToDoItemRepository;
import dev.jonclarke.samplerestservice.models.ToDoItem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class SampleRestServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SampleRestServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ToDoItemRepository repository) {
		// Insert some sample data
		return (args) -> {
			// save a few items
			repository.save(new ToDoItem("Paint Shed", "The garden shed needs a tidy up", LocalDateTime.parse("2023-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME)));
			repository.save(new ToDoItem("Cut the Grass", "Mow the lawn", LocalDateTime.parse("2023-12-31T23:59:59", DateTimeFormatter.ISO_DATE_TIME)));
		};
	}
}
