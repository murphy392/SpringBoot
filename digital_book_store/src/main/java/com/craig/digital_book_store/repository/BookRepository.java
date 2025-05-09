package com.craig.digital_book_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craig.digital_book_store.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);
    
}
