package io.github.brckjr.ppmp.app.shared;

import io.github.brckjr.ppmp.api.shared.AllocationSliceDto;
import io.github.brckjr.ppmp.domain.model.shared.allocation.AllocationSlice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AllocationSliceDtoMapper {

  default AllocationSliceDto toDto(AllocationSlice<?> source) {
    if (source == null) {
      return null;
    }

    String name = null;
    if (source.name() != null) {
      name = source.name() instanceof Enum<?> enumValue
          ? enumValue.name()
          : source.name().toString();
    }

    BigDecimal value = source.percentage() == null ? null : source.percentage();
    return new AllocationSliceDto(name, value);
  }
}
