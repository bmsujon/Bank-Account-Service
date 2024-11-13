package com.bank.account.core.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserLoginRequest {
    @Size(min = 6, max = 30)
    @NotBlank(message = "username required.")
    private String userName;
    @Size(min = 6, max = 20)
    @NotBlank(message = "password required.")
    private String password;
}
