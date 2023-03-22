package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.database.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Listing, Long> {

}
