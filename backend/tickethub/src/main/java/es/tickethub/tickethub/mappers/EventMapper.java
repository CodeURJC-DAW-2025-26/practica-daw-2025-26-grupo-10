package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface EventMapper {
    EventDTO toDTO(Event event);
    EventBasicDTO toBasicDTO(Event event);
    
    @Mapping(source = "artist.artistID", target = "artist.artistID")
    Event toEntity(EventDTO eventDTO);
}