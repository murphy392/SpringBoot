package com.craig.digital_book_store.testcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

import com.craig.digital_book_store.testexceptions.TestBookNotFoundException;
import com.craig.digital_book_store.testmodel.TestBook;
import com.craig.digital_book_store.testservice.TestBookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
@Component("testBookController")
public class TestBookController {
    @Autowired
    private final TestBookService bookService;
    
    public TestBookController(TestBookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<TestBook>> findAll() {
        List<TestBook> books = bookService.getAll();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<TestBook> findById(@PathVariable("id") Long id) {
        Optional<TestBook> book = bookService.findById(id);
        return ResponseEntity.of(book);

    }

    @GetMapping("/findByTitle/{title}")
    public List<TestBook> findByTitle(@PathVariable("title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/findByAuthor/{author}")
    public List<TestBook> findByAuthor(@PathVariable("author") String author) {
        return bookService.findByAuthor(author);
    }


    @PostMapping("/addBook")
    public TestBook create(@RequestBody @Valid TestBook book) {
        return bookService.create(book);
    }
    
 @PutMapping("/updateBook/{id}")
    public ResponseEntity<TestBook> updateBook(@PathVariable("id") Long id, @Valid @RequestBody TestBook updateBook) {
        try {
            TestBook savedBook = bookService.updateBook(id, updateBook);
            return ResponseEntity.ok(savedBook);
        } catch (TestBookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<TestBook> delete(@PathVariable("id") Long id) {
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
