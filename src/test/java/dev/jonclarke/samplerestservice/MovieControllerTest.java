package dev.jonclarke.samplerestservice;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.jonclarke.samplerestservice.dataaccess.MovieRepository;
import dev.jonclarke.samplerestservice.models.MovieDataModel;
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
 * Unit Tests for the MovieController.
 * Tests cover all methods available in the controller
 * Tests cover using all methods with json and xml
 */
@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper jsonMapper;
    XmlMapper xmlMapper;
    @MockBean
    private MovieRepository repository;

    public MovieControllerTest() {
        // We can't autowire the xmlMapper, so we have to create it manually
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    //******************************************************************
    // List all unit tests
    //******************************************************************

    @Test
    public void listAll_EmptyDataSet_ExpectEmptyJsonObject() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
    @Test
    public void listAllAsXml_EmptyDataSet_ExpectEmptyXmlObject() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/movies")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(content().xml("<List/>"));
    }

    @Test
    public void listAll_DataSetContainsOneItem_ExpectDataInJsonObject() throws Exception {
        MovieDataModel item = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        List<MovieDataModel> items = List.of(item);
        when(repository.findAll()).thenReturn(items);

        this.mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId())))
                .andExpect(jsonPath("$[0].title", is(item.getTitle())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].releaseDate", is(formatDateTimeForComparison(item.getReleaseDate()))))
                .andExpect(jsonPath("$[0].availableOnDvd", is(item.isAvailableOnDvd())));
    }

    @Test
    public void listAllAsXml_DataSetContainsOneItem_ExpectDataInXmlObject() throws Exception {
        MovieDataModel item = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        List<MovieDataModel> items = List.of(item);
        when(repository.findAll()).thenReturn(items);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/movies")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/List/*").nodeCount(is(1)))
                .andExpect(xpath("/List/item[1]/id").string(is("" + item.getId())))
                .andExpect(xpath("/List/item[1]/title").string(is(item.getTitle())))
                .andExpect(xpath("/List/item[1]/description").string(is(item.getDescription())))
                .andExpect(xpath("/List/item[1]/releaseDate").string(is(formatDateTimeForComparison(item.getReleaseDate()))))
                .andExpect(xpath("/List/item[1]/availableOnDvd").string(is(item.isAvailableOnDvd().toString())));
    }

    //******************************************************************
    // Get one unit tests
    //******************************************************************

    @Test
    public void getOne_RequestAnItemById_ExpectDataInJsonObject() throws Exception {
        MovieDataModel itemToReturn = buildMovie(456, "test title", "test description", LocalDateTime.now(), false);

        when(repository.findById(itemToReturn.getId())).thenReturn(Optional.of(itemToReturn));

        this.mockMvc.perform(get("/movies/" + itemToReturn.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToReturn.getId())))
                .andExpect(jsonPath("$.title", is(itemToReturn.getTitle())))
                .andExpect(jsonPath("$.description", is(itemToReturn.getDescription())))
                .andExpect(jsonPath("$.releaseDate", is(formatDateTimeForComparison(itemToReturn.getReleaseDate()))))
                .andExpect(jsonPath("$.availableOnDvd", is(itemToReturn.isAvailableOnDvd())));
    }

    @Test
    public void getOneAsXml_RequestAnItemById_ExpectDataInXmlObject() throws Exception {
        MovieDataModel itemToReturn = buildMovie(456, "test title", "test description", LocalDateTime.now(), false);
        when(repository.findById(itemToReturn.getId())).thenReturn(Optional.of(itemToReturn));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/movies/" + itemToReturn.getId())
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/*").nodeCount(is(1)))
                .andExpect(xpath("/Movie[1]/id").string(is("" + itemToReturn.getId())))
                .andExpect(xpath("/Movie[1]/title").string(is(itemToReturn.getTitle())))
                .andExpect(xpath("/Movie[1]/description").string(is(itemToReturn.getDescription())))
                .andExpect(xpath("/Movie[1]/releaseDate").string(is(formatDateTimeForComparison(itemToReturn.getReleaseDate()))))
                .andExpect(xpath("/Movie[1]/availableOnDvd").string(is(itemToReturn.isAvailableOnDvd().toString())));
    }

    @Test
    public void getOne_RequestAnInvalidItem_Expect404Error() throws Exception {
        when(repository.findById(123)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/movies/123"))
                .andExpect(status().isNotFound());
    }

    //******************************************************************
    // Add unit tests
    //******************************************************************

    @Test
    public void add_SaveValidItem_ExpectSuccess() throws Exception {
        MovieDataModel itemToSave = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);

        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.jsonMapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToSave.getId())))
                .andExpect(jsonPath("$.title", is(itemToSave.getTitle())))
                .andExpect(jsonPath("$.description", is(itemToSave.getDescription())))
                .andExpect(jsonPath("$.releaseDate", is(formatDateTimeForComparison(itemToSave.getReleaseDate()))))
                .andExpect(jsonPath("$.availableOnDvd", is(itemToSave.isAvailableOnDvd())));
    }

    @Test
    public void add_PassInvalidItem_ExpectFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("INVALID");

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addAsXml_SaveValidItem_ExpectSuccess() throws Exception {
        MovieDataModel itemToSave = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);

        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content(this.xmlMapper.writeValueAsString(itemToSave));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/*").nodeCount(is(1)))
                .andExpect(xpath("/Movie[1]/id").string(is("" + itemToSave.getId())))
                .andExpect(xpath("/Movie[1]/title").string(is(itemToSave.getTitle())))
                .andExpect(xpath("/Movie[1]/description").string(is(itemToSave.getDescription())))
                .andExpect(xpath("/Movie[1]/releaseDate").string(is(formatDateTimeForComparison(itemToSave.getReleaseDate()))))
                .andExpect(xpath("/Movie[1]/availableOnDvd").string(is(itemToSave.isAvailableOnDvd().toString())));
    }

    //******************************************************************
    // Update unit tests
    //******************************************************************

    @Test
    public void update_SaveValidItem_ExpectSuccess() throws Exception {
        MovieDataModel originalItem = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        MovieDataModel itemToSave = buildMovie(123, "new title", "new description", LocalDateTime.now(), true);

        when(repository.findById(originalItem.getId())).thenReturn(Optional.of(originalItem));
        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies/" + itemToSave.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.jsonMapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(itemToSave.getId())))
                .andExpect(jsonPath("$.title", is(itemToSave.getTitle())))
                .andExpect(jsonPath("$.description", is(itemToSave.getDescription())))
                .andExpect(jsonPath("$.releaseDate", is(formatDateTimeForComparison(itemToSave.getReleaseDate()))))
                .andExpect(jsonPath("$.availableOnDvd", is(itemToSave.isAvailableOnDvd())));
    }

    @Test
    public void update_PassInvalidItem_ExpectFailure() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies/123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("INVALID");

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_InvalidId_ExpectFailure() throws Exception {
        MovieDataModel originalItem = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        MovieDataModel itemToSave = buildMovie(456, "new title", "new description", LocalDateTime.now(), true);

        when(repository.findById(originalItem.getId())).thenReturn(Optional.of(originalItem));
        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies/" + itemToSave.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.jsonMapper.writeValueAsString(itemToSave));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MovieNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find movie " + itemToSave.getId(), result.getResolvedException().getMessage()));
    }

    @Test
    public void updateAsXml_SaveValidItem_ExpectSuccess() throws Exception {
        MovieDataModel originalItem = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        MovieDataModel itemToSave = buildMovie(123, "new title", "new description", LocalDateTime.now(), true);

        when(repository.findById(originalItem.getId())).thenReturn(Optional.of(originalItem));
        when(repository.save(itemToSave)).thenReturn(itemToSave);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies/" + itemToSave.getId())
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content(this.xmlMapper.writeValueAsString(itemToSave));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/*").nodeCount(is(1)))
                .andExpect(xpath("/Movie[1]/id").string(is("" + itemToSave.getId())))
                .andExpect(xpath("/Movie[1]/title").string(is(itemToSave.getTitle())))
                .andExpect(xpath("/Movie[1]/description").string(is(itemToSave.getDescription())))
                .andExpect(xpath("/Movie[1]/releaseDate").string(is(formatDateTimeForComparison(itemToSave.getReleaseDate()))))
                .andExpect(xpath("/Movie[1]/availableOnDvd").string(is(itemToSave.isAvailableOnDvd().toString())));
    }

    //******************************************************************
    // delete unit tests
    //******************************************************************

    @Test
    public void delete_DeleteValidItem_ExpectSuccess() throws Exception {
        MovieDataModel movieToDelete = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);

        when(repository.findById(movieToDelete.getId())).thenReturn(Optional.of(movieToDelete));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/movies/" + movieToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void delete_InvalidId_ExpectFailure() throws Exception {
        MovieDataModel movieToDelete = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);
        int invalidId = 456;

        when(repository.findById(movieToDelete.getId())).thenReturn(Optional.of(movieToDelete));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/movies/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MovieNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find movie " + invalidId, result.getResolvedException().getMessage()));
    }

    @Test
    public void deleteAsXml_DeleteValidItem_ExpectSuccess() throws Exception {
        MovieDataModel movieToDelete = buildMovie(123, "test title", "test description", LocalDateTime.now(), false);

        when(repository.findById(movieToDelete.getId())).thenReturn(Optional.of(movieToDelete));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/movies/" + movieToDelete.getId())
                .contentType(MediaType.APPLICATION_XML);

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    //******************************************************************
    // helper methods
    //******************************************************************

    /**
     * Build a movie to use in these unit tests
     * @param id id value to set
     * @param title title of the movie
     * @param description description of the movie
     * @param releaseDate release date of movie
     * @param availableOnDvd available on Dvd flag
     * @return new Movie object populated with the values
     */
    private MovieDataModel buildMovie(int id, String title, String description, LocalDateTime releaseDate, Boolean availableOnDvd) {
        return new MovieDataModel() {
            {
                setId(id);
                setTitle(title);
                setDescription(description);
                setReleaseDate(releaseDate);
                setAvailableOnDvd(availableOnDvd);
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
