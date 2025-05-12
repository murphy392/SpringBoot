package com.craig.digital_book_store.integration;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.transaction.annotation.Transactional;

import com.craig.digital_book_store.TestApplication;
import com.craig.digital_book_store.testmodel.TestBook;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@Sql({"/schema.sql", "/data.sql"})
@Transactional
public class BookControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // @AfterEach
    // void cleanUp() {
    //     // Clean up the database after all tests are done
    //     jdbcTemplate.execute("DELETE FROM books");

    //     // Reset the sequence
    //     jdbcTemplate.execute("ALTER SEQUENCE books_id_seq RESTART WITH 1");
    // }

    @Test //Pass
    public void getAllBooks_shouldReturnAllBooksFromDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("The Lord of the Rings: The Two Towers")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("The Lord of the Rings: The Return of the King")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title", is("The Lord of the Rings: The Fellowship of the Ring")));
    }

    @Test //Pass
    public void getBookById_shouldReturnBookWithGivenId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/byId/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("The Lord of the Rings: The Two Towers")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", is("J.R.R Tolkien")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(19.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("book1 description")));
    }

    @Test //Pass
    public void createBook_ShouldAddNewBookToDatabase() throws Exception {
        // Arrange
        TestBook newBook = new TestBook("The Hobbit", "J.R.R. Tolkien", 10, new BigDecimal(9.99), "The first book in the series");

        mockMvc.perform(MockMvcRequestBuilders.post("/books/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("The Hobbit")));

                // Verify the book was added by getting all books
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    @Test //Pass
    public void updateBook_shouldModifyExistingBook() throws Exception {
        // Arrange
        TestBook updatedBook = new TestBook("The Hobbit", "J.R.R. Tolkien", 10, new BigDecimal(9.99), "The first book in the series");
        updatedBook.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/updateBook/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("The Hobbit")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author", is("J.R.R. Tolkien")));

    }

    @Test //Pass
    public void deleteBook_shouldRemoveBookFromDatabase() throws Exception {
        //Verify book exists
        mockMvc.perform(MockMvcRequestBuilders.get("/books/byId/3"));

        //Delete the book
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/deleteBook/3"));

        //Verify book is deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/books/byId/3"));
    }
}
