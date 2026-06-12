package io.github.brckjr.ppmp.domain.model.portfolio;

public sealed interface AllocationSlice<T> permits SectorAllocationSlice, AssetAllocationSlice, GeographicAllocationSlice {
    T name();
    Double percentage();
}