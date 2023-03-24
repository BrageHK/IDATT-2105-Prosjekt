package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.filter.SearchSpecification;
import edu.ntnu.idatt2105.backend.model.Listing;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    Optional<Listing> findById(long id);

}
