package edu.ntnu.idatt2105.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The request body for changing the password of a user.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditRequest {

    private String oldPassword;
    private String newPassword;
}
