package dev.jonclarke.samplerestservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a to do item cannot be found in the system
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ToDoItemNotFoundException extends RuntimeException {
    ToDoItemNotFoundException(Integer id) {
        super("Could not find todo item " + id);
    }
}
