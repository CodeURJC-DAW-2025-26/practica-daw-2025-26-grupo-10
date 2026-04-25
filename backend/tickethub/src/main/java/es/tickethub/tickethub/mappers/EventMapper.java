package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class} ) 
public interface EventMapper {
    EventDTO toDTO(Event event);

    @Mapping(source = "sessions", target = "sessions")
    @Mapping(target = "mainImage", expression = "java(referenceMapper.eventToMainImage(event))")
    EventBasicDTO toBasicDTO(Event event);
    
    @Mapping(source = "artistId", target = "artist")
    Event toEntity(EventCreateDTO createEventDTO);

    @Mapping(source = "artistId", target = "artist")
    @Mapping(source = "discountIds", target = "discounts")
    @Mapping(source = "zones", target = "zones")
    @Mapping(source = "sessions", target = "sessions")
    void updateEntityFromDto(EventUpdateDTO dto, @MappingTarget Event event);    
}