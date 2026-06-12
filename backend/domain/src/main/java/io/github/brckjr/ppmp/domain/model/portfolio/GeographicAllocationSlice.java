package io.github.brckjr.ppmp.domain.model.portfolio;

import io.github.brckjr.ppmp.domain.enums.Region;

public record GeographicAllocationSlice(Region name, Double percentage) implements AllocationSlice<Region> {
}

