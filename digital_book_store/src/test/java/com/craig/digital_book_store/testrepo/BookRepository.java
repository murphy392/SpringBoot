package com.craig.digital_book_store.testrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craig.digital_book_store.testmodel.Book;


public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);
    
}
