package com.craig.digital_book_store.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.craig.digital_book_store.model.Book;
import com.craig.digital_book_store.repository.BookRepository;

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

    public Optional<Book> updateBook(Long id, Book book){
        return repo.findById(id)
            .map(oldBook -> {
                Book updated = oldBook.updateBook(book);
                return repo.save(updated);
            });
    }

    public void removeBook(Long id) {
       repo.deleteById(id);
    }
}
