package com.renatmirzoev.wallet.mapper;

import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public class MapperConfig {
}
