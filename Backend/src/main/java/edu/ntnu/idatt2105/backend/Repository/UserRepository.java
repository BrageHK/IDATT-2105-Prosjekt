package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO, Long> {

    Optional<UserDTO> findByEmail(String email);
}
