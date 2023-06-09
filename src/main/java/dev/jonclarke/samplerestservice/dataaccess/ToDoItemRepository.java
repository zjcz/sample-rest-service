package dev.jonclarke.samplerestservice.dataaccess;

import dev.jonclarke.samplerestservice.models.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {
}
