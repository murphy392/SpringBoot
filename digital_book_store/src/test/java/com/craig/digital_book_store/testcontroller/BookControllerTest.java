package com.craig.digital_book_store.testcontroller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.craig.digital_book_store.controller.BookController;
import com.craig.digital_book_store.testexceptions.BookNotFoundException;
import com.craig.digital_book_store.testmodel.Book;
import com.craig.digital_book_store.testrepo.BookRepository;
import com.craig.digital_book_store.testservice.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(BookController.class)
public class BookControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository repo;

    @Test //Pass
    void getAll_shouldReturnListOfBooks() throws Exception {
        //Arrange
        List<Book> books = Arrays.asList(
            new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series"),
            new Book("The Lord of the Rings: The Two Towers", "J.R.R Tolkien", 20, new BigDecimal(7.99), "Second book in the series"),
            new Book("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien", 10, new BigDecimal(99.00), "Special edition of the third book in the series")
        );

        when(bookService.getAll()).thenReturn(books);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Lord of the Rings: The Fellowship of the Ring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(5.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("First book in the series"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("The Lord of the Rings: The Two Towers"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(7.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Second book in the series"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("The Lord of the Rings: The Return of the King"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value("J.R.R. Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].quantity").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].price").value(99.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value("Special edition of the third book in the series"));

                verify(bookService, times(1)).getAll();
    }

    @Test //Pass
    void getAll_shouldREturnEmptyList_whenNoBooksExist() throws Exception {
        //Arrange
        when(bookService.getAll()).thenReturn(Collections.emptyList());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(bookService, times(1)).getAll();
    }

    @Test //Pass
    void findById_shouldReturnBook_whenIdExists() throws Exception{
        //Arrange
        Long id = 1L;
        Book book = new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        when(repo.findById(id)).thenReturn(Optional.of(book));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/byId{id}", id))
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
        when(bookService.findById(itemId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/byId{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).findById(itemId);
    }

    @Test //Pass
    void findByTitle_shouldReturnBookList_whenTitleExists() throws Exception {
        //Arrange
        String title = "The Lord of the Rings";
        Map<Long, Book> books = new HashMap<>();
            books.put(1L, new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series"));
            books.put(2L, new Book("The Lord of the Rings: The Two Towers", "J.R.R Tolkien", 20, new BigDecimal(7.99), "Second book in the series"));
            books.put(3L,new Book("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien", 10, new BigDecimal(99.00), "Special edition of the third book in the series"));

        when(repo.findByTitle(title)).thenReturn(Optional.of(books));

        //Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/findByTitle/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
         // Use TypeReference to specify the type of the Map
        TypeReference<HashMap<Long, Book>> typeReference = new TypeReference<HashMap<Long, Book>>() {};
        Map<Long, Book> returnedBooks = objectMapper.readValue(content, typeReference);

            assertThat(returnedBooks).hasSize(3);
            assertThat(returnedBooks).containsKey(1L);
            assertThat(returnedBooks.get(1L).getId()).isEqualTo(1L);
            assertThat(returnedBooks.get(1L).getTitle()).isEqualTo("The Lord of the Rings: The Fellowship of the Ring");
            assertThat(returnedBooks.get(1L).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(1L).getQuantity()).isEqualTo(30);
            assertThat(returnedBooks.get(1L).getPrice()).isEqualTo(new BigDecimal(5.99));
            assertThat(returnedBooks.get(1L).getDescription()).isEqualTo("First book in the series");

            assertThat(returnedBooks).containsKey(2L);
            assertThat(returnedBooks.get(2L).getId()).isEqualTo(2L);
            assertThat(returnedBooks.get(2L).getTitle()).isEqualTo("The Lord of the Rings: The Two Towers");
            assertThat(returnedBooks.get(2L).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(2L).getQuantity()).isEqualTo(20);
            assertThat(returnedBooks.get(2L).getPrice()).isEqualTo(new BigDecimal(7.99));
            assertThat(returnedBooks.get(2L).getDescription()).isEqualTo("Second book in the series");

            assertThat(returnedBooks).containsKey(3L);
            assertThat(returnedBooks.get(3L).getId()).isEqualTo(3L);
            assertThat(returnedBooks.get(3L).getTitle()).isEqualTo("The Lord of the Rings: The Return of the King");
            assertThat(returnedBooks.get(3L).getAuthor()).isEqualTo("J.R.R. Tolkien"); //Set incorrectly for the author test cases
            assertThat(returnedBooks.get(3L).getQuantity()).isEqualTo(10);
            assertThat(returnedBooks.get(3L).getPrice()).isEqualTo(new BigDecimal(99.00));
            assertThat(returnedBooks.get(3L).getDescription()).isEqualTo("Special edition of the third book in the series");
    }

    @Test //Pass
    void findByTitle_shouldReturnNoContent_whenTitleDoesNotExist() throws Exception{
        String title = "Harry Potter and the Sorcerer's Stone";
        when(bookService.findByTitle(title)).thenReturn(Collections.emptyMap());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/findByTitle/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk());

                verify(bookService, times(1)).findByTitle(title);
    }

    @Test //Possibly a false pass. Double check my logic as only 2 books should have been returned
    void findByAuthor_shouldReturnBookList_whenAuthorExists() throws Exception {
        //Arrange
        String author = "J.R.R Tolkien";
        Map<Long, Book> books = new HashMap<>();
            books.put(1L, new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series"));
            books.put(2L, new Book("The Lord of the Rings: The Two Towers", "J.R.R Tolkien", 20, new BigDecimal(7.99), "Second book in the series"));
            books.put(3L,new Book("The Lord of the Rings: The Return of the King", "J.R.R. Tolkien", 10, new BigDecimal(99.00), "Special edition of the third book in the series"));

        when(bookService.findByAuthor(author)).thenReturn(books);

        //Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/findByAuthor/{author}", author))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
         // Use TypeReference to specify the type of the Map
        TypeReference<HashMap<Long, Book>> typeReference = new TypeReference<HashMap<Long, Book>>() {};
        Map<Long, Book> returnedBooks = objectMapper.readValue(content, typeReference);

            assertThat(returnedBooks).hasSize(3);
            assertThat(returnedBooks).containsKey(1L);
            assertThat(returnedBooks.get(1L).getId()).isEqualTo(1L);
            assertThat(returnedBooks.get(1L).getTitle()).isEqualTo("The Lord of the Rings: The Fellowship of the Ring");
            assertThat(returnedBooks.get(1L).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(1L).getQuantity()).isEqualTo(30);
            assertThat(returnedBooks.get(1L).getPrice()).isEqualTo(new BigDecimal(5.99));
            assertThat(returnedBooks.get(1L).getDescription()).isEqualTo("First book in the series");

            assertThat(returnedBooks).containsKey(2L);
            assertThat(returnedBooks.get(2L).getId()).isEqualTo(2L);
            assertThat(returnedBooks.get(2L).getTitle()).isEqualTo("The Lord of the Rings: The Two Towers");
            assertThat(returnedBooks.get(2L).getAuthor()).isEqualTo("J.R.R Tolkien");
            assertThat(returnedBooks.get(2L).getQuantity()).isEqualTo(20);
            assertThat(returnedBooks.get(2L).getPrice()).isEqualTo(new BigDecimal(7.99));
            assertThat(returnedBooks.get(2L).getDescription()).isEqualTo("Second book in the series");

            assertThat(returnedBooks).containsKey(3L);
            assertThat(returnedBooks.get(3L).getId()).isEqualTo(3L);
            assertThat(returnedBooks.get(3L).getTitle()).isEqualTo("The Lord of the Rings: The Return of the King");
            assertThat(returnedBooks.get(3L).getAuthor()).isEqualTo("J.R.R. Tolkien"); //Set incorrectly for the author test cases
            assertThat(returnedBooks.get(3L).getQuantity()).isEqualTo(10);
            assertThat(returnedBooks.get(3L).getPrice()).isEqualTo(new BigDecimal(99.00));
            assertThat(returnedBooks.get(3L).getDescription()).isEqualTo("Special edition of the third book in the series");
    }

    @Test // Pass
    void findByAuthor_shouldReturnNoContent_whenAuthorDoesNotExist() throws Exception {
        String author = "J.K. Rowling";
        when(bookService.findByTitle(author)).thenReturn(Collections.emptyMap());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/books/findByAuthor/{author}", author))
                .andExpect(MockMvcResultMatchers.status().isOk());

                verify(bookService, times(1)).findByAuthor(author);
    }

    @Test //Pass
    void create_shouldReturnCreatedBook_andStatusCodeCreated() throws Exception {
        //Arrange
        Book newBook = new Book(1L, "The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        Book savedBook = new Book(1L, "The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");
        when(bookService.create(any(Book.class))).thenReturn(savedBook);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:9000/books/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings: The Fellowship of the Ring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.R.R Tolkien"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First book in the series"));

                verify(bookService, times(1)).create(any(Book.class));
    }

    @Test //Pass
    void updateBook_shouldReturnUpdatedBook_whenIdExists() throws Exception {
        //Arrange
        Long bookId = 1L;
        Book updatedBook = new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 15, new BigDecimal(7.99), "Test");
        Book existingBook = new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 30, new BigDecimal(5.99), "First book in the series");

        when(bookService.findById(bookId)).thenReturn(existingBook);
        when(bookService.updateBook(eq(bookId), any(Book.class))).thenReturn(new Book("The Lord of the Rings: The Fellowship of the Ring", "J.R.R Tolkien", 15, new BigDecimal(7.99), "First book in the series"));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:9000/books/updateBook/{id}", bookId)
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

                verify(bookService, times(1)).findById(bookId);
                verify(bookService, times(1)).updateBook(eq(bookId), any(Book.class));
    }

    @Test //Pass
    void deleteBook_shouldReturnNoContent_whenIdExists() throws Exception{
        //Arrange
        Long id = 1L;
        doNothing().when(bookService).removeBook(id);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:9000/books/deleteBook/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(bookService, times(1)).removeBook(id);
    }

    @Test //Pass
    void deleteBook_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        //Arrange
        Long bookId = 1L;
        doThrow(new BookNotFoundException("Book not found")).when(bookService).removeBook(bookId);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:9000/books/deleteBook/{id}", bookId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).removeBook(bookId);
    }
}
