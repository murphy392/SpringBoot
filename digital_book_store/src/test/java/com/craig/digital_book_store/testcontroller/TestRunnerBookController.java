package com.craig.digital_book_store.testcontroller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.craig.digital_book_store.testexceptions.TestBookNotFoundException;
import com.craig.digital_book_store.testexceptions.TestGlobalExceptionHandler;
import com.craig.digital_book_store.testmodel.TestBook;
import com.craig.digital_book_store.testservice.TestBookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


// Specifically target the test controller, not the real one
// @WebMvcTest(TestBookController.class)
// @Import({TestConfig.class, TestJpaConfig.class})
public class TestRunnerBookController {

    private MockMvc mockMvc;

    @Mock
    private TestBookService bookService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private TestBookController bookController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
        .setControllerAdvice(new TestGlobalExceptionHandler())
        .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAll_shouldReturnListOfBooks() throws Exception {
        // Arrange
        TestBook book1 = new TestBook("Test Book 1", "Test Author 1", 20, new BigDecimal(19.99), "book1 description");
        TestBook book2 = new TestBook("Test Book 2", "Test Author 2", 12, new BigDecimal(20.99), "book 2 description");
        List<TestBook> books = Arrays.asList(book1, book2);
        book1.setId(1L);
        book2.setId(2L);
        
        when(bookService.getAll()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author", is("Test Author 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", is(20)))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(content);
                    double price = rootNode.path(0).path("price").asDouble();
                    org.junit.jupiter.api.Assertions.assertEquals(19.99, price, 0.01);
            })
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("book1 description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Test Book 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author", is("Test Author 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity", is(12)))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(content);
                    double price = rootNode.path(1).path("price").asDouble();
                    org.junit.jupiter.api.Assertions.assertEquals(20.99, price, 0.01);
            })
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("book 2 description")));
        
        verify(bookService, times(1)).getAll();
    }

    @Test //Pass
    void getAll_shouldREturnEmptyList_whenNoBooksExist() throws Exception {
        //Arrange
        when(bookService.getAll()).thenReturn(Collections.emptyList());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(bookService, times(1)).getAll();
    }

    @Test //Pass
    void findById_shouldReturnBook_whenIdExists() throws Exception{
        //Arrange
        Long id = 1L;
        TestBook book = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        book.setId(id);
        when(bookService.findById(id)).thenReturn(Optional.of(book));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books/byId/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings: The Fellowship of the Ring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First book in the series"));

        verify(bookService, times(1)).findById(id);
    }

    @Test //Pass
    void findById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Arrange
        Long itemId = 1L;
        when(bookService.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books/byId/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).findById(itemId);
    }

    @Test //Pass
    void findByTitle_shouldReturnBookList_whenTitleExists() throws Exception {
        //Arrange
        String title = "The Lord of the Rings";

        TestBook book1 = new TestBook("Test Book 1", "Test Author 1", 20, new BigDecimal(19.99), "book1 description");
        TestBook book2 = new TestBook("Test Book 2", "Test Author 2", 12, new BigDecimal(20.99), "book 2 description");
        TestBook book3 = new TestBook("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien", 10, new BigDecimal(99.00), "Special edition of the third book in the series");
        book1.setId(1L);
        book2.setId(2L);
        book3.setId(3L);

        List<TestBook> books = Arrays.asList(book1, book2, book3);

        when(bookService.findByTitle(title)).thenReturn(books);

        //Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books/findByTitle/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        TypeReference<List<TestBook>> typeReference = new TypeReference<List<TestBook>>() {};
        List<TestBook> returnedBooks = objectMapper.readValue(content, typeReference);

            assertThat(returnedBooks).hasSize(3);
            assertThat(returnedBooks.get(0).getId()).isEqualTo(1L);
            assertThat(returnedBooks.get(0).getTitle()).isEqualTo("Test Book 1");
            assertThat(returnedBooks.get(0).getAuthor()).isEqualTo("Test Author 1");
            assertThat(returnedBooks.get(0).getQuantity()).isEqualTo(20);
            assertThat(returnedBooks.get(0).getPrice()).isEqualTo(new BigDecimal(19.99));
            assertThat(returnedBooks.get(0).getDescription()).isEqualTo("book1 description");

            assertThat(returnedBooks.get(1).getId()).isEqualTo(2L);
            assertThat(returnedBooks.get(1).getTitle()).isEqualTo("Test Book 2");
            assertThat(returnedBooks.get(1).getAuthor()).isEqualTo("Test Author 2");
            assertThat(returnedBooks.get(1).getQuantity()).isEqualTo(12);
            assertThat(returnedBooks.get(1).getPrice()).isEqualTo(new BigDecimal(20.99));
            assertThat(returnedBooks.get(1).getDescription()).isEqualTo("book 2 description");

            assertThat(returnedBooks.get(2).getId()).isEqualTo(3L);
            assertThat(returnedBooks.get(2).getTitle()).isEqualTo("The Lord of the Rings: The Return of the King");
            assertThat(returnedBooks.get(2).getAuthor()).isEqualTo("J.R.R. Tolkien");
            assertThat(returnedBooks.get(2).getQuantity()).isEqualTo(10);
            assertThat(returnedBooks.get(2).getPrice()).isEqualTo(new BigDecimal(99.00));
            assertThat(returnedBooks.get(2).getDescription()).isEqualTo("Special edition of the third book in the series");
    }

    @Test //Pass
    void findByTitle_shouldReturnNoContent_whenTitleDoesNotExist() throws Exception{
        String title = "Harry Potter and the Sorcerer's Stone";
        when(bookService.findByTitle(title)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/findByTitle/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk());

                verify(bookService, times(1)).findByTitle(title);
    }

    @Test //Pass
    void findByAuthor_shouldReturnBookList_whenAuthorExists() throws Exception {
        //Arrange
        TestBook book1 = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        TestBook book2 = new TestBook("The Lord of the Rings: The Two Towers", "J.R.R Tolkien", 20, new BigDecimal(7.99), "Second book in the series");
        TestBook book3 = new TestBook("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien", 10, new BigDecimal(99.00), "Special edition of the third book in the series");
        book1.setId(1L);
        book2.setId(2L);
        book3.setId(3L);
        List<TestBook> books = Arrays.asList(book1, book2);

        String author = "J.R.R Tolkien";

        when(bookService.findByAuthor(author)).thenReturn(books);

        //Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books/findByAuthor/{author}", author))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        TypeReference<List<TestBook>> typeReference = new TypeReference<List<TestBook>>() {};
        List<TestBook> returnedBooks = objectMapper.readValue(content, typeReference);

            assertThat(returnedBooks).hasSize(2);
            assertThat(returnedBooks.get(0).getId()).isEqualTo(1L);
            assertThat(returnedBooks.get(0).getTitle()).isEqualTo("The Lord of the Rings: The Fellowship of the Ring");
            assertThat(returnedBooks.get(0).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(0).getQuantity()).isEqualTo(30);
            assertThat(returnedBooks.get(0).getPrice()).isEqualTo(new BigDecimal(5.99));
            assertThat(returnedBooks.get(0).getDescription()).isEqualTo("First book in the series");

            assertThat(returnedBooks.get(1).getId()).isEqualTo(2L);
            assertThat(returnedBooks.get(1).getTitle()).isEqualTo("The Lord of the Rings: The Two Towers");
            assertThat(returnedBooks.get(1).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(1).getQuantity()).isEqualTo(20);
            assertThat(returnedBooks.get(1).getPrice()).isEqualTo(new BigDecimal(7.99));
            assertThat(returnedBooks.get(1).getDescription()).isEqualTo("Second book in the series");

            verify(bookService, times(1)).findByAuthor(author);
    }

    @Test // Pass
    void findByAuthor_shouldReturnNoContent_whenAuthorDoesNotExist() throws Exception {
        String author = "J.K. Rowling";
        when(bookService.findByTitle(author)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/findByAuthor/{author}", author))
                .andExpect(MockMvcResultMatchers.status().isOk());

                verify(bookService, times(1)).findByAuthor(author);
    }

    @Test //Pass
    void create_shouldReturnCreatedBook_andStatusCodeCreated() throws Exception {
        //Arrange
        TestBook newBook = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        TestBook savedBook = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        newBook.setId(1L);
        savedBook.setId(1L);
        when(bookService.create(any(TestBook.class))).thenReturn(savedBook);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:9000/books/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings: The Fellowship of the Ring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First book in the series"));

                verify(bookService, times(1)).create(any(TestBook.class));
    }

    @Test //
    void updateBook_shouldReturnUpdatedBook_whenIdExists() throws Exception {
        //Arrange
        Long bookId = 1L;
        TestBook updatedBook = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 15, new BigDecimal(7.99), "Test");
        TestBook existingBook = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        updatedBook.setId(bookId);
        existingBook.setId(bookId);

        // Create the return book and set its ID
        TestBook returnedBook = new TestBook("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 15, new BigDecimal(7.99), "First book in the series");
        returnedBook.setId(bookId);

        when(bookService.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookService.updateBook(eq(bookId), any(TestBook.class))).thenReturn(returnedBook);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/books/updateBook/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings: The Fellowship of the Ring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(7.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First book in the series"));

                verify(bookService, times(1)).updateBook(eq(bookId), any(TestBook.class));
    }

    @Test //Pass
    void deleteBook_shouldReturnNoContent_whenIdExists() throws Exception{
        //Arrange
        Long id = 1L;
        doNothing().when(bookService).removeBook(id);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/deleteBook/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(bookService, times(1)).removeBook(id);
    }

    @Test //Pass
    void deleteBook_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        //Arrange
        Long bookId = 1L;
        doThrow(new TestBookNotFoundException("Book not found")).when(bookService).removeBook(bookId);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/deleteBook/{id}", bookId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).removeBook(bookId);
    }
}
