package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.SessionBasicDTO;
import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Session;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    @Mapping(source = "event.eventID", target = "event") 
    SessionDTO toDTO(Session session);

    SessionBasicDTO toBasicDTO(Session session);

    @Mapping(source = "event.eventID", target = "event")
    List<SessionDTO> toDTOs(Collection<Session> sessions);
}