package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    EventDTO toDTO(Event event);
    
    EventBasicDTO toBasicDTO(Event event);

    List<EventDTO> toDTOs(Collection<Event> events);

    @Mapping(target = "eventID", ignore = true)
    Event toDomain(EventDTO eventDTO);

    // Transforming EventBasicDTO to Event -> used in other mappers
    default Event fromBasic(EventBasicDTO basic) {
        if (basic == null) return null;
        Event event = new Event();
        event.setEventID(basic.eventID());
        return event;
    }
}