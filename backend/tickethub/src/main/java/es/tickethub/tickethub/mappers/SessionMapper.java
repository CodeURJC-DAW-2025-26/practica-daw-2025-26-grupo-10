package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Session;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    
    SessionDTO toDTO(Session session);

    List <SessionDTO> toDTOs(Collection <Session> sessions);

    @Mapping(target = "sessionID", ignore = true)
    Session toDomain(SessionDTO sessionDTO);

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
