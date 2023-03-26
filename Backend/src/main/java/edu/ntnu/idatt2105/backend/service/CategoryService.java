package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    public ResponseEntity<String> getAllCategories() throws JsonProcessingException {
        if(categoryRepository.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Category> categories = categoryRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok().body(mapper.writeValueAsString(categories));
    }

    /**
     * Deletes a category from the database. Only an admin can delete a category.
     * @return
     *//*
    public ResponseEntity<String> removeCategory() {
        if(!authenticationService.isAdmin()) {
            return ResponseEntity.status(401).build();
        }
        if(categoryRepository.findAll().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        categoryRepository.delete(categoryRepository.findAll().get(0));
        return ResponseEntity.ok("Successfully deleted category");
    }*/
}
