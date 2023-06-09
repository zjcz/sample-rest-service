package dev.jonclarke.samplerestservice;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jonclarke.samplerestservice.dataaccess.ToDoItemRepository;
import dev.jonclarke.samplerestservice.models.ToDoItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Unit Tests for the ToDoController.  Tests cover all methods available in the controller
 */
@WebMvcTest(ToDoController.class)
public class ToDoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ToDoItemRepository repository;

    //******************************************************************
    // List all unit tests
    //******************************************************************

    @Test
    public void listAll_EmptyDataSet_ExpectEmptyJsonObject() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void listAll_DataSetContainsOneItem_ExpectDataInJsonObject() throws Exception {
        ToDoItem item = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);
        List<ToDoItem> items = List.of(item);
        when(repository.findAll()).thenReturn(items);

        this.mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId())))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].dueDate", is(formatDateTimeForComparison(item.getDueDate()))))
                .andExpect(jsonPath("$[0].completed", is(item.getCompleted())));
    }

    //******************************************************************
    // Get one unit tests
    //******************************************************************

    @Test
    public void getOne_RequestAnItemById_ExpectDataInJsonObject() throws Exception {
        ToDoItem itemToReturn = buildToDoItem(456, "test item", "test description", LocalDateTime.now(), false);

        when(repository.findById(itemToReturn.getId())).thenReturn(Optional.of(itemToReturn));

        this.mockMvc.perform(get("/todo/" + itemToReturn.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToReturn.getId())))
                .andExpect(jsonPath("$.name", is(itemToReturn.getName())))
                .andExpect(jsonPath("$.description", is(itemToReturn.getDescription())))
                .andExpect(jsonPath("$.dueDate", is(formatDateTimeForComparison(itemToReturn.getDueDate()))))
                .andExpect(jsonPath("$.completed", is(itemToReturn.getCompleted())));
    }

    @Test
    public void getOne_RequestAnInvalidItem_Expect404Error() throws Exception {
        when(repository.findById(123)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/todo/123"))
                .andExpect(status().isNotFound());
    }

    //******************************************************************
    // Add unit tests
    //******************************************************************

    @Test
    public void add_SaveValidItem_ExpectSuccess() throws Exception {
        ToDoItem itemToSave = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);

        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToSave.getId())))
                .andExpect(jsonPath("$.name", is(itemToSave.getName())))
                .andExpect(jsonPath("$.description", is(itemToSave.getDescription())))
                .andExpect(jsonPath("$.dueDate", is(formatDateTimeForComparison(itemToSave.getDueDate()))))
                .andExpect(jsonPath("$.completed", is(itemToSave.getCompleted())));
    }

    @Test
    public void add_PassInvalidItem_ExpectFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("INVALID");

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    //******************************************************************
    // Update unit tests
    //******************************************************************

    @Test
    public void update_SaveValidItem_ExpectSuccess() throws Exception {
        ToDoItem originalItem = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);
        ToDoItem itemToSave = buildToDoItem(123, "new item", "new description", LocalDateTime.now(), true);

        when(repository.findById(originalItem.getId())).thenReturn(Optional.of(originalItem));
        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/todo/" + itemToSave.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToSave.getId())))
                .andExpect(jsonPath("$.name", is(itemToSave.getName())))
                .andExpect(jsonPath("$.description", is(itemToSave.getDescription())))
                .andExpect(jsonPath("$.dueDate", is(formatDateTimeForComparison(itemToSave.getDueDate()))))
                .andExpect(jsonPath("$.completed", is(itemToSave.getCompleted())));
    }

    @Test
    public void update_PassInvalidItem_ExpectFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/todo/123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("INVALID");

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_InvalidId_ExpectFailure() throws Exception {
        ToDoItem originalItem = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);
        ToDoItem itemToSave = buildToDoItem(456, "new item", "new description", LocalDateTime.now(), true);

        when(repository.findById(originalItem.getId())).thenReturn(Optional.of(originalItem));
        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/todo/" + itemToSave.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ToDoItemNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find todo item " + itemToSave.getId(), result.getResolvedException().getMessage()));
    }

    //******************************************************************
    // delete unit tests
    //******************************************************************

    @Test
    public void delete_DeleteValidItem_ExpectSuccess() throws Exception {
        ToDoItem itemToDelete = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);

        when(repository.findById(itemToDelete.getId())).thenReturn(Optional.of(itemToDelete));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/todo/" + itemToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void delete_InvalidId_ExpectFailure() throws Exception {
        ToDoItem itemToDelete = buildToDoItem(123, "test item", "test description", LocalDateTime.now(), false);
        int invalidId = 456;

        when(repository.findById(itemToDelete.getId())).thenReturn(Optional.of(itemToDelete));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/todo/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ToDoItemNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find todo item " + invalidId, result.getResolvedException().getMessage()));
    }

    /**
     * Build a toDoItem to use in these unit tests
     * @param id id value to set
     * @param name name of the item
     * @param description description of the item
     * @param completed completed flag
     * @return new ToDoItem populated with the values
     */
    private ToDoItem buildToDoItem(int id, String name, String description, LocalDateTime dueDateTime, Boolean completed) {
        return new ToDoItem() {
            {
                setId(id);
                setName(name);
                setDescription(description);
                setDueDate(dueDateTime);
                setCompleted(completed);
            }
        };
    }

    /**
     * Format the LocalDateTime object for comparison.  The internal LocalDateTime.toString() formats the milliseconds
     * to 9 places and pads with zeros but the format from jsonPath is only to 7, meaning the 2 don't match
     * @param dt LocalDateTime to format
     * @return formatted date, in the format of yyyy-MM-dd'T'HH:mm:ss.SSSSSSS
     */
    private String formatDateTimeForComparison(LocalDateTime dt) {
        if (dt == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        return dt.format(formatter).replaceAll("[0]*$", ""); //remove trailing zeros
    }

    //******************************************************************
    // helper method unit tests
    //******************************************************************

    @Test
    public void formatDateTimeForComparison_MillisecondsWithNoTrailingZeros_NoChangeToDateTime() {
        String dateTimeWithNoTrailingZeros = "2023-12-31T23:59:59.1234567";
        assertEquals("2023-12-31T23:59:59.1234567", formatDateTimeForComparison(LocalDateTime.parse(dateTimeWithNoTrailingZeros)));
    }
    @Test
    public void formatDateTimeForComparison_MillisecondsWithTrailingZeros_TrailingZerosRemoved() {
        String dateTimeWithTrailingZeros = "2023-12-31T23:59:59.1234500";
        assertEquals("2023-12-31T23:59:59.12345",  formatDateTimeForComparison(LocalDateTime.parse(dateTimeWithTrailingZeros)));
    }
    @Test
    public void formatDateTimeForComparison_MillisecondsContainingZeros_NoChangeToDateTime() {
        String dateTimeContainingZeros = "2023-12-31T23:59:59.1230045";
        assertEquals("2023-12-31T23:59:59.1230045", formatDateTimeForComparison(LocalDateTime.parse(dateTimeContainingZeros)));
    }
}
