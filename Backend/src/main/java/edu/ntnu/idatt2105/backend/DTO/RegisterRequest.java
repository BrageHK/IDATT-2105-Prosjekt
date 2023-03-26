package edu.ntnu.idatt2105.backend.DTO;

import edu.ntnu.idatt2105.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The request body for registering a user.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private Long phonenumber;
    private Role role;
}
