package edu.ntnu.idatt2105.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The request body for changing the password of a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditRequest {

    private String oldPassword;
    private String newPassword;
}
