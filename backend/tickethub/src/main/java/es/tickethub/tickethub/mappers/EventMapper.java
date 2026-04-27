package es.tickethub.tickethub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.EventBasicDTO;
import es.tickethub.tickethub.dto.EventCreateDTO;
import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.dto.EventUpdateDTO;
import es.tickethub.tickethub.entities.Event;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class} ) 
public interface EventMapper {
    EventDTO toDTO(Event event);

    @Mapping(source = "sessions", target = "sessions")
    @Mapping(target = "mainImage", expression = "java(referenceMapper.eventToMainImage(event))")
    EventBasicDTO toBasicDTO(Event event);
    
    @Mapping(source = "artistId", target = "artist")
    Event toEntity(EventCreateDTO createEventDTO);

    @Mapping(source = "artistId", target = "artist")
    @Mapping(target = "discounts", ignore = true)
    @Mapping(target = "zones", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    void updateEntityFromDto(EventUpdateDTO dto, @MappingTarget Event event);    
}