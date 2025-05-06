package com.craig.digital_book_store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.craig.digital_book_store.model.Book;

@Service
public class BookService {
    private final Map<Long, Book> books = new HashMap<>();
    private Long nextId = 1L;

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public Map<Long, Book> findByTitle(String title){
        //Need code reusability. Get an iterable collection of book objects to find by title in THIS method
        //the collection of books needs to be returned in it's own private method in this class
        Map<Long, Book> matchingTitle = new HashMap<>();
        for (Entry<Long, Book> entry: books.entrySet()) {
            Book foundBook = entry.getValue();
            if (foundBook.getTitle().toLowerCase().contains(title)){
                matchingTitle.put(entry.getKey(), foundBook);
            }
        }
        return matchingTitle;

    }

    //Sample code:
    // private Map<String, Book> filterBooks(Map<Integer, Book> bookMap) { 
    //     Map<String, Book> filteredBooks = new HashMap<>();
    //     for (Map.Entry<Integer, Book> entry : bookMap.entrySet()) {
    //         Book book = entry.getValue();
    //         if (book.getTitle().toLowerCase().contains("the lord of the rings")) {
    //             filteredBooks.put(String.valueOf(entry.getKey()), book); // You might want a more meaningful key
    //         }
    //     }
    //     return filteredBooks;
    // }

    public Map<Long, Book> findByAuthor(String author){
        //Need code reusability. Get an iterable collection of book objects to find by title in THIS method
        //the collection of books needs to be returned in it's own private method in this class
        Map<Long, Book> matchingAuthor = new HashMap<>();
        for (Entry<Long, Book> entry: books.entrySet()) {
            Book foundBook = entry.getValue();
            if (foundBook.getTitle().toLowerCase().contains(author)){
                matchingAuthor.put(entry.getKey(), foundBook);
            }
        }
        return matchingAuthor;
    }

    public Book create(Book book){
        book.setId(nextId++);
        books.put(book.getId(), book);
        return book;
    }

    public Book updateBook(Long id, Book book){
        if (books.containsKey(id)) {
            book.setId(id);
            books.put(id, book);
            return book;
        }
        return null; // Plan for exception logic
    }

    public void removeBook(Long id) {
        //Implement different logic
        books.remove(id);
    }
}
