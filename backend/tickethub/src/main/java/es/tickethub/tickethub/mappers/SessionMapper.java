package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = {EventMapper.class, PurchaseMapper.class})
public interface SessionMapper {

    @Mapping(target = "event", source = "event") 
    SessionDTO toDTO(Session session);

    SessionBasicDTO toBasicDTO(Session session);
    List<SessionDTO> toDTOs(Collection<Session> sessions);

    @Mapping(target = "sessionID", ignore = true)
    @Mapping(target = "event", source = "event") 
    Session toDomain(SessionDTO sessionDTO);

    default Long eventToLong(Event event) {
        return event != null ? event.getEventID() : null;
    }

    default Event longToEvent(Long eventID) {
        if (eventID == null) return null;
        Event event = new Event();
        event.setEventID(eventID);
        return event;
    }

    default Session fromBasic(SessionBasicDTO basic) {
        if (basic == null) return null;
        Session session = new Session();
        session.setSessionID(basic.sessionID());
        return session;
    }
}