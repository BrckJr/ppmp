package io.github.brckjr.ppmp.api.auth.dto;

import io.github.brckjr.ppmp.common.annotation.Required;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
  @Required @NotBlank @Size(max = 320) String identifier,
  @Required @NotBlank @Size(max = 72) String password
) {
}
