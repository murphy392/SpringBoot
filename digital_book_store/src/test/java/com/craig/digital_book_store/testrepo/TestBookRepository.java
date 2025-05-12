package com.craig.digital_book_store.testrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craig.digital_book_store.testmodel.TestBook;

@Repository
public interface TestBookRepository extends JpaRepository<TestBook, Long> {
    
    List<TestBook> findByTitle(String title);

    List<TestBook> findByAuthor(String author);
    
}
