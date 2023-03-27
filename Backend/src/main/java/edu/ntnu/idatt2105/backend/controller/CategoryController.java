package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ntnu.idatt2105.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for categories.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get all categories as a JSON.
     *
     * @return all categories as a JSON
     */
    @Operation(summary = "Get all categories", description = "Get all categories as a JSON.")
    @GetMapping("/getAllCategories")
    public ResponseEntity<String> getAllCategories() throws JsonProcessingException {
        return categoryService.getAllCategories();
    }

    /**
     * Deletes a category from the database. Only an admin can delete a category.
     *
     * @return a response entity with a message
     */
    @Operation(summary = "Delete a category", description = "Deletes a category from the database." +
            " Only an admin can delete a category.")
    @GetMapping("/removeCategory/{id}")
    public ResponseEntity<String> removeCategory(@PathVariable Long id) {
        return categoryService.removeCategory(id);
    }

    /**
     * Adds a category to the database. Only an admin can add a category.
     *
     * @param
     */
    @Operation(summary = "Add a category", description = "Adds a category to the database." +
            " Only an admin can add a category.")
    @PostMapping("/addCategory/{name}")
    public ResponseEntity<String> addCategory(@PathVariable String name) {
        return categoryService.addCategory(name);
    }
}
