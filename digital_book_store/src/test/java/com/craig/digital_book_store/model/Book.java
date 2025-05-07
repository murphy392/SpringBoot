package com.craig.digital_book_store.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

public class Book {
    private Long id;
    private String title;
    private String author;
    private int quantity;
    private BigDecimal price;
    private String description;

    public Book(Long id, String title, String author, int quantity, BigDecimal price, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }


    @Id
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity(){
        return quantity;
    }


    public BigDecimal getPrice(){
        return price;
    }


    public String getDescription(){
        return description;
    }

    public Book updateBook(Book book){
        return new Book(this.id, book.title, book.author, book.quantity, book.price, book.description);
    }
}
