package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.entities.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {
    
    EventDTO toDTO(Event event);

    List <EventDTO> toDTOs(Collection <Event> events);

    @Mapping(target = "eventID", ignore = true)
    Event toDomain(EventDTO eventDTO);
}
