package dev.jonclarke.samplerestservice.models;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * To Do item entity object.
 */

@JacksonXmlRootElement(localName="ToDoItem")
@Entity
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private LocalDateTime dueDateTime;
    private Boolean completed;

    public ToDoItem() {

    }

    public ToDoItem(String name, String description, LocalDateTime dueDateTime) {
        this.name = name;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDateTime;
    }

    public void setDueDate(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof ToDoItem)) {
            return false;
        }

        ToDoItem item = (ToDoItem) o;
        return Objects.equals(this.id, item.id) && Objects.equals(this.name, item.name)
                && Objects.equals(this.description, item.description)
                && Objects.equals(this.dueDateTime, item.dueDateTime)
                && Objects.equals(this.completed, item.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.completed);
    }

    @Override
    public String toString() {
        return "ToDoItem{" + "id=" + this.id + ", name='" + this.name + '\''
                + ", description='" + this.description + '\''
                + ", dueDateTime='" + this.dueDateTime + '\''
                + ", completed='" + this.completed + '\'' + '}';
    }


}

