package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = {TicketMapper.class, EventMapper.class})
public interface ZoneMapper {

    ZoneDTO toDTO(Zone zone);
    ZoneBasicDTO toBasicDTO(Zone zone);
    List<ZoneDTO> toDTOs(Collection<Zone> zones);

    @Mapping(target = "id", ignore = true)
    Zone toDomain(ZoneDTO zoneDTO);

    default Zone fromBasic(ZoneBasicDTO basic) {
        if (basic == null) return null;
        Zone zone = new Zone();
        zone.setId(basic.id());
        return zone;
    }
}