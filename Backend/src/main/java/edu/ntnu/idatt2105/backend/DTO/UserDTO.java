package edu.ntnu.idatt2105.backend.DTO;

import edu.ntnu.idatt2105.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The request body for editing a user. The user is identified by the id. Also used for transferring user data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Long phoneNumber;
    private String address;

}