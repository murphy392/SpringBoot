package  com.craig.digital_book_store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.craig.digital_book_store.exceptions.BookNotFoundException;
import com.craig.digital_book_store.model.Book;

@Service
public class BookService{
    // private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final Map<Long, Book> books = new HashMap<>();
    private Long nextId = 1L;

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public Book findById(Long id){
        return books.get(id);
    }

    public Map<Long, Book> findByTitle(String title){
        Map<Long, Book> matchingTitle = new HashMap<>();
        for (Entry<Long, Book> entry : books.entrySet()) {
            Book foundBookTest = entry.getValue();
            if (foundBookTest.getTitle().toLowerCase().contains(title)){
                matchingTitle.put(entry.getKey(), foundBookTest);
            }
        }
        return matchingTitle;
    }

    public Map<Long, Book> findByAuthor(String author) {
        Map<Long, Book> matchingAuthor = new HashMap<>();
        for (Entry<Long, Book> entry : books.entrySet()) {
            Book foundBookTest = entry.getValue();
            if (foundBookTest.getAuthor().toLowerCase().contains(author)) {
                matchingAuthor.put(entry.getKey(), foundBookTest);
            }
        }
        return matchingAuthor;
    }

    public Book create(Book book) {
        book.setId(nextId++);
        this.books.put(book.getId(), book);
        return book;
    }

    public Book updateBook(Long id, Book book) {
        if (books.containsKey(id)) {
            book.setId(id);
            books.put(id, book);
            return book;
        }
        return null; //Plan for exception logic
    }

    public void removeBook(Long id) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        books.remove(id);
    }
}