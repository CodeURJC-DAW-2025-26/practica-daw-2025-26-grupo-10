package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.dto.TicketBasicDTO;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = { ZoneMapper.class, PurchaseMapper.class })
public interface TicketMapper {

    TicketDTO toDTO(Ticket ticket);

    TicketBasicDTO toBasicDTO(Ticket ticket);

    List<TicketDTO> toDTOs(Collection<Ticket> tickets);

    @Mapping(target = "ticketID", ignore = true)
    Ticket toDomain(TicketDTO ticketDTO);

    default Ticket fromBasic(TicketBasicDTO basic) {
        if (basic == null) return null;
        Ticket ticket = new Ticket();
        ticket.setTicketID(basic.ticketID());
        return ticket;
    }

    default Long zoneToLong(Zone zone) {
        return zone != null ? zone.getId() : null;
    }

    default Zone longToZone(Long zoneID) {
        if (zoneID == null) return null;
        Zone zone = new Zone();
        zone.setId(zoneID);
        return zone;
    }

    default Long purchaseToLong(Purchase purchase) {
        return purchase != null ? purchase.getPurchaseID() : null;
    }

    default Purchase longToPurchase(Long purchaseID) {
        if (purchaseID == null) return null;
        Purchase purchase = new Purchase();
        purchase.setPurchaseID(purchaseID);
        return purchase;
    }
}