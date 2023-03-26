package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ntnu.idatt2105.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Logger logger = org.slf4j.LoggerFactory.getLogger(CategoryController.class);
        logger.info("Getting all categories");
        return categoryService.getAllCategories();
    }
}
