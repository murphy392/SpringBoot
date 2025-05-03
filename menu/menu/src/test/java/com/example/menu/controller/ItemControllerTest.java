package com.example.menu.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.menu.model.Item;
import com.example.menu.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void findAll_shouldReturnListOfItems() throws Exception {
        // Arrange
        List<Item> items = Arrays.asList(
                new Item(1L, "Item 1", 599L, "Description 1", "random string"),
                new Item(2L, "Item 2", 299L, "Description 2", "random string")
        );
        when(itemService.findAll()).thenReturn(items);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/menu/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Item 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Item 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Description 2"));

        verify(itemService, times(1)).findAll();
    }

    @Test
    void getAllItems_shouldReturnEmptyList_whenNoItemsExist() throws Exception {
        // Arrange
        when(itemService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/menu/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(itemService, times(1)).findAll();
    }

    @Test
    void find_shouldReturnItem_whenIdExists() throws Exception {
        // Arrange
        Long itemId = 1L;
        Item item = new Item(itemId, "Test Item", itemId, "Test Description", null);
        when(itemService.find(itemId)).thenReturn(item);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/menu/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"));

        verify(itemService, times(1)).find(itemId);
    }

    @Test
    void getItemById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Arrange
        Long itemId = 1L;
        when(itemService.find(itemId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/menu/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(itemService, times(1)).find(itemId);
    }

    @Test
    void create_shouldReturnCreatedItem_andStatusCodeCreated() throws Exception {
        // Arrange
        Item newItem = new Item(null, "New Item", null, "New Description", null);
        Item savedItem = new Item(1L, "New Item", null, "New Description", null);
        when(itemService.create(any(Item.class))).thenReturn(savedItem);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/menu/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Or .isCreated() depending on desired API convention
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("New Description"));

        verify(itemService, times(1)).create(any(Item.class));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem_whenIdExists() throws Exception {
        // Arrange
        Long itemId = 1L;
        Item updatedItem = new Item(null, "Updated Item", itemId, "Updated Description", null);
        Item existingItem = new Item(itemId, "Original Item", itemId, "Original Description", null);
        when(itemService.find(itemId)).thenReturn(existingItem);
        when(itemService.updateItem(eq(itemId), any(Item.class))).thenReturn(new Item(itemId, "Updated Item", itemId, "Updated Description", null));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/api/menu/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"));

        verify(itemService, times(1)).find(itemId);
        verify(itemService, times(1)).updateItem(eq(itemId), any(Item.class));
    }

    @Test
    void updateItem_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Arrange
        Long itemId = 1L;
        Item updatedItem = new Item(null, "Updated Item", itemId, "Updated Description", null);
        when(itemService.find(itemId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/api/menu/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(itemService, times(1)).find(itemId);
        verify(itemService, never()).updateItem(eq(itemId), any(Item.class));
    }

    @Test
    void delete_shouldReturnNoContent_whenIdExists() throws Exception {
        // Arrange
        Long itemId = 1L;
        doNothing().when(itemService).delete(itemId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/menu/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(itemService, times(1)).delete(itemId);
    }

    @Test
    void deleteItem_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Arrange
        Long itemId = 1L;
        doThrow(new RuntimeException("Item not found")).when(itemService).delete(itemId); // Simulate item not found

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/menu/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); // Or other appropriate error code

        verify(itemService, times(1)).delete(itemId);
    }
}