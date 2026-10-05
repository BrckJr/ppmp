package io.github.brckjr.ppmp.api.auth.dto;

import io.github.brckjr.ppmp.common.annotation.Required;

import java.util.UUID;

public record UserDto(
  @Required UUID id,
  @Required String username,
  @Required String email
) {
}
