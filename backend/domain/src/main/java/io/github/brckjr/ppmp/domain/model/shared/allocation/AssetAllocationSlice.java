package io.github.brckjr.ppmp.domain.model.shared.allocation;

import io.github.brckjr.ppmp.common.enums.AssetClass;

import java.math.BigDecimal;

public record AssetAllocationSlice(AssetClass name, BigDecimal percentage) implements AllocationSlice<AssetClass> {
}

