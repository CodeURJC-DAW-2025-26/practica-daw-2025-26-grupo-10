package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZoneMapper {

    @Mapping(source = "event.eventID", target = "eventId")
    ZoneDTO toDTO(Zone zone);

    ZoneBasicDTO toBasicDTO(Zone zone);

    @Mapping(source = "event.eventID", target = "eventId")
    List<ZoneDTO> toDTOs(Collection<Zone> zones);

    @Mapping(target = "id", ignore = true)
    Zone toDomain(ZoneDTO zoneDTO);
}