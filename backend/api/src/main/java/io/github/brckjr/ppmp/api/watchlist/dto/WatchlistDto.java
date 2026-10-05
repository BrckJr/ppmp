package io.github.brckjr.ppmp.api.watchlist.dto;

import io.github.brckjr.ppmp.common.annotation.Required;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record WatchlistDto(
  UUID id,
  @Required @NotBlank String name,
  String description,
  Integer itemCount
) {
}
