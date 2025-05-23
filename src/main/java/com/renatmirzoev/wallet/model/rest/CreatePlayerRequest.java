package com.renatmirzoev.wallet.model.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePlayerRequest(
    @NotBlank String fullName,
    @NotBlank @Email String email,
    String phoneNumber
) {
}
