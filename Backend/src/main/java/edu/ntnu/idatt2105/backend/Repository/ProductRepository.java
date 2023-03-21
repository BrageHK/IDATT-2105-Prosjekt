package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.database.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
