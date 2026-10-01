package io.github.brckjr.ppmp.domain.model.shared.allocation;

import io.github.brckjr.ppmp.common.enums.Region;

import java.math.BigDecimal;

public record GeographicAllocationSlice(Region name, BigDecimal percentage) implements AllocationSlice<Region> {
}

