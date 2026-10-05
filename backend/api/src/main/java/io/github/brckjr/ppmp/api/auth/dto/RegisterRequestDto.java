package io.github.brckjr.ppmp.api.auth.dto;

import io.github.brckjr.ppmp.common.annotation.Required;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
  @Required @NotBlank @Size(min = 3, max = 32) String username,
  @Required @NotBlank @Email @Size(max = 320) String email,
  @Required @NotBlank @Size(min = 10, max = 72) String password
) {
}
