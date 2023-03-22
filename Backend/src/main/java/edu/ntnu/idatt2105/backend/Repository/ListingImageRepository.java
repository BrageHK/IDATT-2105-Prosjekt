package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.database.ListingImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListingImageRepository extends JpaRepository<ListingImages, Long> {

    Optional<ListingImages> findById(long id);
}