package com.craig.digital_book_store.testservice;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.craig.digital_book_store.testexceptions.TestBookNotFoundException;
import com.craig.digital_book_store.testmodel.TestBook;
import com.craig.digital_book_store.testrepo.TestBookRepository;

import jakarta.transaction.Transactional;

@Service
public class TestBookService {
    private static final Logger logger = LoggerFactory.getLogger(TestBookService.class);

    @Autowired
    private final TestBookRepository repo;

    public TestBookService(TestBookRepository repo) {
        this.repo = repo;
    }

    public List<TestBook> getAll() {
        return repo.findAll();
    }

    public Optional<TestBook> findById(Long id){
        return repo.findById(id);
    }

    public List<TestBook> findByTitle(String title){
        return repo.findByTitle(title);
    }

    public List<TestBook> findByAuthor(String author){
        return repo.findByAuthor(author);
    }

    public TestBook create(TestBook book){
       TestBook copy = new TestBook(book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(), book.getDescription());
       return repo.save(copy);
    }

    @Transactional
    public TestBook updateBook(Long id, TestBook updatedBook) throws TestBookNotFoundException {
        Optional<TestBook> existingBookOptional = repo.findById(id);
        if (existingBookOptional.isPresent()) {
            TestBook existingBook = existingBookOptional.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setQuantity(updatedBook.getQuantity());
            existingBook.setDescription(updatedBook.getDescription());
            return repo.save(existingBook);
        } else {
            throw new TestBookNotFoundException("Book not found with id: " + id);
        }
    }
    

    public void removeBook(Long id) {
       repo.deleteById(id);
    }
}

