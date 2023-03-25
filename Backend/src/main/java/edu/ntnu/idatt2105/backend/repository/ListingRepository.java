package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    Optional<Listing> findById(long id);

}
