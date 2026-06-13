package io.github.brckjr.ppmp.domain.model.portfolio;

import io.github.brckjr.ppmp.domain.enums.AssetClass;

public record AssetAllocationSlice(AssetClass name, Double percentage) implements AllocationSlice<AssetClass> {
}

