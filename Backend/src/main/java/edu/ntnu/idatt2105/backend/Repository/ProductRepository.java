package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.DTO.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {

}
