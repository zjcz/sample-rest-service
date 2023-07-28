package dev.jonclarke.samplerestservice;

import dev.jonclarke.samplerestservice.dataaccess.ToDoItemRepository;
import dev.jonclarke.samplerestservice.models.ToDoItem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * To Do Rest API Controller
 * Provides basic CRUD functionality for a To-Do list type application.  The actions include
 *  - Get All (HTTP Get)
 *  - Get One (HTTP Get)
 *  - Add (HTTP Post)
 *  - Update (HTTP Put)
 *  - Delete (HTTP Delete)
 */
@RestController
public class ToDoController {

    private final ToDoItemRepository repository;

    ToDoController(ToDoItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/todo", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<ToDoItem> getAllToDoItems() {
        return repository.findAll();
    }

    @GetMapping(value = "/todo/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    ToDoItem getSingleToDoItem(@PathVariable Integer id) {

        return repository.findById(id)
                .orElseThrow(() -> new ToDoItemNotFoundException(id));
    }

    @PostMapping(value = "/todo",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    ToDoItem newToDoItem(@RequestBody ToDoItem newItem) {
        return repository.save(newItem);
    }

    @PutMapping(value = "/todo/{id}",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    ToDoItem updateToDoItem(@RequestBody ToDoItem newToDoItem, @PathVariable Integer id) {

        return repository.findById(id)
                .map(item -> {
                    item.setName(newToDoItem.getName());
                    item.setDescription(newToDoItem.getDescription());
                    item.setDueDate(newToDoItem.getDueDate());
                    item.setCompleted(newToDoItem.getCompleted());
                    return repository.save(item);
                })
                .orElseThrow(() -> new ToDoItemNotFoundException(id));
    }

    @DeleteMapping(value = "/todo/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    void deleteToDoItem(@PathVariable Integer id) {
        if (repository.findById(id).isEmpty()) {
            throw new ToDoItemNotFoundException(id);
        }

        repository.deleteById(id);
    }

}
