package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    Optional<Listing> findById(long id);
}
