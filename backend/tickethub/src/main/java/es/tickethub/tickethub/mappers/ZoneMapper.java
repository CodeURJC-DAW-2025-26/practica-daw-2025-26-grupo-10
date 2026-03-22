package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZoneMapper {

    Zone toDomain(ZoneCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    void updateEntityFromBasicDTO(ZoneBasicDTO dto, @MappingTarget Zone zone);

    ZoneDTO toDTO(Zone zone);

    ZoneBasicDTO toBasicDTO(Zone zone);

    List<ZoneBasicDTO> toBasicDTOs(List<Zone> zones);
}