package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.ZoneDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    ZoneDTO toDTO(Zone zone);

    List <ZoneDTO> toDTOs(Collection <Zone> zones);

    @Mapping(target = "id", ignore = true)
    Zone toDomain(ZoneDTO zoneDTO);

    default Long eventToLong(Event event) {
        return event != null ? event.getEventID() : null;
    }

    default Event longToEvent(Long eventID) {
        if (eventID == null) {
            return null;
        }
        Event event = new Event();
        event.setEventID(eventID);
        return event;
    }

}
