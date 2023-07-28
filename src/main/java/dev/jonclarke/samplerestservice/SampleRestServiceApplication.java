package dev.jonclarke.samplerestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.jonclarke.samplerestservice.dataaccess.MovieRepository;
import dev.jonclarke.samplerestservice.models.MovieDataModel;
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
	public CommandLineRunner demo(MovieRepository repository) {
		// Insert some sample data
		return (args) -> {
			// save a few movies
			repository.save(new MovieDataModel("Movie 1", "Movie 1 Description", LocalDateTime.parse("2023-01-01T01:10:10", DateTimeFormatter.ISO_DATE_TIME), true));
			repository.save(new MovieDataModel("Movie 2", "Movie 2 Description", LocalDateTime.parse("2023-12-31T23:59:59", DateTimeFormatter.ISO_DATE_TIME), false));
		};
	}
}
