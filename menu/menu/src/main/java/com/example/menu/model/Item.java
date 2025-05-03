package com.example.menu.model;

import org.springframework.data.annotation.Id;

public class Item {
    private Long id;
    private String name;
    private Long price;
    private String description;
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
