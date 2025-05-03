package com.example.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.menu.model.Item;
import com.example.menu.service.ItemService;




@RestController
@RequestMapping("api/menu/items")
public class ItemController {
    @Autowired
    private ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    	@GetMapping("/greeting")
	public String greeting() {
		return "Hellow World";
	}

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        List<Item> items = service.findAll();
        return ResponseEntity.ok().body(items);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Item> find(@PathVariable("id") Long id) {
        Item item = service.find(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }    
    }
    
    @PostMapping
    public Item create(@RequestBody Item item) {
        return service.create(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable("id") Long id, @RequestBody Item updatedItem) {
        Item existingItem = service.find(id); // Assuming your service checks existence
        if (existingItem != null) {
            Item savedItem = service.updateItem(id, updatedItem);
            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> delete(@PathVariable("id") Long id){
        try {
            service.delete(id); // Assuming your service method is deleteItem
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Item not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            throw e; // Re-throw other RuntimeExceptions
        }
    }
    
}
