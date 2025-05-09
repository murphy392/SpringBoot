package com.craig.digital_book_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.craig.digital_book_store.model.Book;

import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE  %:title%")
    List<Book> findByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b WHERE b.author LIKE %:author%")
    List<Book> findByAuthor(@Param("author") String author);
    

}
