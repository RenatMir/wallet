package com.renatmirzoev.wallet.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlayerRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
    private String phoneNumber;
}
