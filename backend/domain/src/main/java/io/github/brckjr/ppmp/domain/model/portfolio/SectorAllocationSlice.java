package io.github.brckjr.ppmp.domain.model.portfolio;

import io.github.brckjr.ppmp.domain.enums.Sector;

public record SectorAllocationSlice(Sector name, Double percentage) implements AllocationSlice<Sector> {}

