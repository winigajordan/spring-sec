package co.jordan.usermicroservices.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Nonnull
public class RegistrationDto {
    private String username;
    private String password;
    private String email;
}
