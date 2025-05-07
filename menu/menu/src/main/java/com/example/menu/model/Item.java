package com.example.menu.model;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Item {
    private Long id;

    @NotNull(message="name is required")
    @Pattern(regexp="^[a-zA-Z ]+$", message="name must be a string")
    private String name;

    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private Long price;

    @NotNull(message = "description is required")
    @Pattern(regexp="^[a-zA-Z ]+$", message = "description must be a string")
    private String description;

    @NotNull(message = "image is required")
    @URL(message = "image must be a URL")
    private String image;

    public Item(
        Long id,
        String name, 
        Long price, 
        String description,
        String image
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }
    
    @Id
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getDescription(){
        return description;
    }

    public String getImage() {
        return image;
    }

    public Item updateItem(Item item) {
        return new Item(this.id, item.name, item.price, item.description, item.image);
    }

}
