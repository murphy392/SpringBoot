package com.craig.digital_book_store.service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.craig.digital_book_store.model.Book;

@Service
public class BookService {
    private final Map<Long, Book> books = new HashMap<>();
    private Long nextId = 1L;

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public Book findByTitle(String title){
        // ToDo: implement logic to get ALL books based on the title. There can be multiple
        //books with the same title
        return books.get(id);
    }

    public List findByAuthor(String author){
        //ToDo: implement logic to get ALL books based on the author
        return books.get(id);
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
