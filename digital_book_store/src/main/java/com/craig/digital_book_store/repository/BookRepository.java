package com.craig.digital_book_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craig.digital_book_store.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);
    
}
