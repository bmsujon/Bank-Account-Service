package com.bank.account.core.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserCreationRequest {
    @Size(min = 6, max = 30)
    @NotBlank(message = "username required.")
    private String userName;
    @Size(min = 6, max = 20)
    @NotBlank(message = "password required.")
    private String password;
    private String phoneNo;
    @Email
    private String email;
    @NotBlank(message = "address required")
    private String address;
    @NotBlank(message = "fullName required")
    private String fullName;
    @NotNull(message = "dateOfBirth required")
    private Instant dateOfBirth;
}
