package io.github.brckjr.ppmp.app.dashboard.mapper;

import io.github.brckjr.ppmp.api.dashboard.dto.AllocationSliceDto;
import io.github.brckjr.ppmp.domain.enums.AssetClass;
import io.github.brckjr.ppmp.domain.enums.Region;
import io.github.brckjr.ppmp.domain.enums.Sector;
import io.github.brckjr.ppmp.domain.model.portfolio.AssetAllocationSlice;
import io.github.brckjr.ppmp.domain.model.portfolio.GeographicAllocationSlice;
import io.github.brckjr.ppmp.domain.model.portfolio.SectorAllocationSlice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AllocationSliceDtoMapper {

    @Mapping(target = "value", source = "percentage")
    AllocationSliceDto toDto(AssetAllocationSlice source);

    @Mapping(target = "value", source = "percentage")
    AllocationSliceDto toDto(SectorAllocationSlice source);

    @Mapping(target = "value", source = "percentage")
    AllocationSliceDto toDto(GeographicAllocationSlice source);

    @Mapping(target = "percentage", source = "value")
    AssetAllocationSlice toAssetAllocationSlice(AllocationSliceDto source);

    @Mapping(target = "percentage", source = "value")
    SectorAllocationSlice toSectorAllocationSlice(AllocationSliceDto source);

    @Mapping(target = "percentage", source = "value")
    GeographicAllocationSlice toGeographicAllocationSlice(AllocationSliceDto source);

    default BigDecimal map(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    default String map(Object value) {
        return value == null ? null : value.toString();
    }

    default Double map(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    default AssetClass mapToAssetClass(String name) {
        return name == null ? null : AssetClass.valueOf(name);
    }

    default Sector mapToSector(String name) {
        return name == null ? null : Sector.valueOf(name);
    }

    default Region mapToRegion(String name) {
        return name == null ? null : Region.valueOf(name);
    }
}
