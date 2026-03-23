package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.dto.TicketBasicDTO;
import es.tickethub.tickethub.entities.Ticket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketDTO toDTO(Ticket ticket);

    TicketBasicDTO toBasicDTO(Ticket ticket);

    List<TicketDTO> toDTOs(Collection<Ticket> tickets);

    @Mapping(target = "ticketID", ignore = true)
    Ticket toDomain(TicketDTO ticketDTO);
}