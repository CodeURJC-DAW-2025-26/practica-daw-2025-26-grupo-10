package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    @Mapping(source = "event.eventID", target = "event") 
    SessionDTO toDTO(Session session);

    SessionBasicDTO toBasicDTO(Session session);

    @Mapping(source = "event.eventID", target = "event")
    List<SessionDTO> toDTOs(Collection<Session> sessions);
}