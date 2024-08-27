package com.project.homeListener.login.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record RegisterRequestDTO(@NotBlank String userId,
                                 @NotBlank String password,
                                 @Email @NotBlank String email) {
}
