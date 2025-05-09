package com.craig.digital_book_store.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.craig.digital_book_store.exceptions.BookNotFoundException;
import com.craig.digital_book_store.model.Book;
import com.craig.digital_book_store.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
@EnableJpaRepositories
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    // @Autowired
    // private BookRepository repo;

    public List<Book> getAll() {
        return repo.findAll();
    }

    public Optional<Book> findById(Long id){
        return repo.findById(id);
    }

    public List<Book> findByTitle(String title){
        return repo.findByTitle(title);
    }

    public List<Book> findByAuthor(String author){
        return repo.findByAuthor(author);
    }

    public Book create(Book book){
       Book copy = new Book(book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), book.getDescription());
       return repo.save(copy);
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) throws BookNotFoundException {
        Optional<Book> existingBookOptional = repo.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setQuantity(updatedBook.getQuantity());
            existingBook.setDescription(updatedBook.getDescription());
            return repo.save(existingBook);
        } else {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
    }
    

    public void removeBook(Long id) {
       repo.deleteById(id);
    }
}
