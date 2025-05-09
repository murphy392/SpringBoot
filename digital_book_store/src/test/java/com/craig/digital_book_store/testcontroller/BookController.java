package com.craig.digital_book_store.testcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craig.digital_book_store.testmodel.Book;
import com.craig.digital_book_store.testservice.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        List<Book> books = bookService.getAll();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/byId{id}")
    public ResponseEntity<Book> findById(@PathVariable("id") Long id) {
        Optional<Book> book = bookService.findById(id);
        return ResponseEntity.of(book);

    }

    @GetMapping("/findByTitle/{title}")
    public List<Book> findByTitle(@PathVariable("title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/findByAuthor/{author}")
    public List<Book> findByAuthor(@PathVariable("author") String author) {
        return bookService.findByAuthor(author);
    }


    @PostMapping("/addBook")
    public Book create(@RequestBody @Valid Book book) {
        return bookService.create(book);
    }
    
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book updateBook) {
        Optional<Book> existingBook = bookService.findById(id);
        if (existingBook.isPresent()) {
            Optional<Book> updated = bookService.updateBook(id, updateBook);
            return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Book> delete(@PathVariable("id") Long id) {
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e){
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });

        return ResponseEntity.badRequest().body(map);

    }
}
