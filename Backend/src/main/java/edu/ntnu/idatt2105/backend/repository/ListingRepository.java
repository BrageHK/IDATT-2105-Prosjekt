package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * The repository for the listing entity. This repository is used to access the database.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    Optional<Listing> findById(long id);

}
