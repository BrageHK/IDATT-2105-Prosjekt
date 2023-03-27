package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service for the category entity. This service is used to access the database.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthenticationService authenticationService;

    /**
     * Returns a list of all categories in the database.
     *
     * @return a list of all categories in the database
     * @throws JsonProcessingException if the list of categories cannot be converted to json
     */
    public ResponseEntity<String> getAllCategories() throws JsonProcessingException {
        if(categoryRepository.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Category> categories = categoryRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok().body(mapper.writeValueAsString(categories));
    }

    /**
     * Removes a category from the database. Only an admin can delete a category.
     *
     * @param categoryId the id of the category to be deleted
     * @return a response entity with a message
     */
    public ResponseEntity<String> removeCategory(long categoryId) {
        if(!authenticationService.isAdmin()) {
            return ResponseEntity.status(401).build();
        }
        if(categoryRepository.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        if(!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.badRequest().build();
        }
        categoryRepository.delete(categoryRepository.findById(categoryId).get());
        return ResponseEntity.ok("Successfully deleted category");
    }

    /**
     * Adds a category to the database. Only an admin can add a category.
     *
     * @param name the name of the category to be added
     * @return a response entity with a message
     */
    public ResponseEntity<String> addCategory(String name) {
        if(!authenticationService.isAdmin()) {
            return ResponseEntity.status(401).build();
        }
        Category category = Category.builder().name(name).build();
        categoryRepository.save(category);
        return ResponseEntity.ok("Successfully added the category: " + name);
    }

    /**
     * Deletes a category from the database. Only an admin can delete a category.
     *
     * @return a response entity with a message
     */
    public ResponseEntity<String> removeCategory() {
        if(!authenticationService.isAdmin()) {
            return ResponseEntity.status(401).build();
        }
        if(categoryRepository.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        categoryRepository.delete(categoryRepository.findAll().get(0));
        return ResponseEntity.ok("Successfully deleted category");
    }
}
