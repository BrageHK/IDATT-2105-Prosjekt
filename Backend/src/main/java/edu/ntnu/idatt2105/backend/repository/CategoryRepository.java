package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The repository for the category entity. This repository is used to access the database.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
