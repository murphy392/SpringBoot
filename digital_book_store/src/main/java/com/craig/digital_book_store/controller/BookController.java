package com.craig.digital_book_store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.craig.digital_book_store.model.Book;
import com.craig.digital_book_store.service.BookService;

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

    //Update the below to return a List<Book> as there can be multiple books with the same title
    @GetMapping("/{title}")
    public Book findByTitle(@PathVariable("title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/{author}")
    public List<Book> findByAuthor(@PathVariable("author") String author) {
        return bookService.findByAuthor(author);
    }
    

    @PostMapping("/addBook")
    public Book create(@Valid @RequestBody Book book) {
        return bookService.create(book);
    }
    
    @PutMapping("/updateBook/{id}")
    public Book updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book updateBook) {   
        return bookService.updateBook(id, updateBook);
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
