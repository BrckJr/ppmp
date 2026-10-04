package io.github.brckjr.ppmp.api.watchlist.dto;

import io.github.brckjr.ppmp.common.annotation.Required;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record WatchlistDto(
  UUID id,
  @Required @NotBlank @Size(max = 100) String name,
  @Size(max = 255) String description,
  Integer itemCount
) {
}
