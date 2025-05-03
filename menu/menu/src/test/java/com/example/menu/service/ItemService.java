package com.example.menu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.menu.model.Item;

@Service
public class ItemService {

    private final Map<Long, Item> items = new HashMap<>();
    private Long nextId = 1L;

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public Item find(Long id) {
        return items.get(id);
    }

    public Item create(Item item) {
        item.setId(nextId++);
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Long id, Item item) {
        if (items.containsKey(id)) {
            item.setId(id); // Ensure the ID is set
            items.put(id, item);
            return item;
        }
        return null; // Or throw an exception
    }

    public void delete(Long id) {
        items.remove(id);
    }

    //Below is code I'd like to get working
    // @Autowired
    // private final CrudRepository<Item, Long> repository;

    // public ItemService(CrudRepository<Item, Long> repository){
    //     this.repository = repository;
    //     this.repository.saveAll(defaultItems());
    // }

    // private static List<Item> defaultItems() {
    //     return List.of(
    //         new Item(1L, "Burger", 599L, "Tasty", "https://cdn.auth0.com/blog/whatabyte/burger-sm.png"),
    //         new Item(2L, "Pizza", 299L, "Cheesy", "https://cdn.autho0.com/blog/whatabyte/pizza-sm.png"),
    //         new Item(3L, "Tea", 199L, "Informative", "https://cdn.auth0.com/blog/whatabyte/tea-sm.png")
    //     );
    // }

    // public List<Item> findAll() {
    //     List<Item> list = new ArrayList<>();
    //     Iterable<Item> items = repository.findAll();
    //     items.forEach(list::add);
    //     return list;
    // }

    // public Optional<Item> find(Long id) {
    //     return repository.findById(id);
    // }

    // public Item create(Item item){
    //     Item copy = new Item(
    //         new Date().getTime(), 
    //         item.getName(), 
    //         item.getPrice(), 
    //         item.getDescription(), 
    //         item.getImage());
            
    //     return repository.save(copy);
    // }
    
    // public Optional<Item> update(Long id, Item newItem){
    //     return repository.findById(id)
    //         .map(oldItem -> {
    //             Item updated = oldItem.updateItem(newItem);
    //             return repository.save(updated);
    //         });
    // }

    // public void delete(Long id){
    //     repository.deleteById(id);
    // }
}
