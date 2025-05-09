package com.craig.digital_book_store.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="title is required")
    @Column(name = "title")
    private String title;

    @NotNull(message="author is required")
    @Column(name = "author")
    private String author;

    @NotNull(message="quantity is required")
    @Positive(message="quantity must be positive or 0") //Fix to allow for 0 quantity
    @Column(name = "quantity")
    private int quantity;

    @NotNull(message="price is required")
    @Positive(message="price must be positive or 0.00")
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    public Book(String title, String author, int quantity, BigDecimal price, String description){
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

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
        return new Book(book.title, book.author, book.quantity, book.price, book.description);
    }
}
