package io.github.brckjr.ppmp.domain.model.shared.allocation;

import java.math.BigDecimal;

public sealed interface AllocationSlice<T> permits SectorAllocationSlice, AssetAllocationSlice, GeographicAllocationSlice {
  T name();

  BigDecimal percentage();
}