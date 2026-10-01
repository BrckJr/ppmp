package io.github.brckjr.ppmp.domain.model.shared.allocation;

import io.github.brckjr.ppmp.common.enums.Sector;

import java.math.BigDecimal;

public record SectorAllocationSlice(Sector name, BigDecimal percentage) implements AllocationSlice<Sector> {
}