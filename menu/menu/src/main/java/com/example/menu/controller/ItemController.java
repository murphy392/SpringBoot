package com.example.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Item find(@PathVariable("id") Long id) {
        return service.find(id);
    }
    
    @PostMapping
    public Item create(@RequestBody Item item) {
        return service.create(item);
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable("id") Long id, @RequestBody Item updatedItem) {
        return service.updateItem(id, updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
