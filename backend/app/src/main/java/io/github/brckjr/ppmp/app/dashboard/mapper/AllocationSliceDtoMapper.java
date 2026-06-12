package io.github.brckjr.ppmp.app.dashboard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {})
public interface AllocationSliceDtoMapper {


}
