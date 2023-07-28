package dev.jonclarke.samplerestservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a movie cannot be found in the system
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {
    MovieNotFoundException(Integer id) {
        super("Could not find movie " + id);
    }
}
