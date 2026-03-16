package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.PurchaseDTO;
import es.tickethub.tickethub.dto.PurchaseBasicDTO;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { TicketMapper.class })
public interface PurchaseMapper {
    
    PurchaseDTO toDTO(Purchase purchase);

    PurchaseBasicDTO toBasicDTO(Purchase purchase);

    List<PurchaseDTO> toDTOs(Collection<Purchase> purchases);

    @Mapping(target = "purchaseID", ignore = true)
    Purchase toDomain(PurchaseDTO purchaseDTO);

    default Purchase fromBasic(PurchaseBasicDTO basic) {
        if (basic == null) return null;
        Purchase purchase = new Purchase();
        purchase.setPurchaseID(basic.purchaseID());
        return purchase;
    }

    default Long sessionToLong(Session session) {
        return session != null ? session.getSessionID() : null;
    }

    default Session longToSession(Long sessionID) {
        if (sessionID == null) return null;
        Session session = new Session();
        session.setSessionID(sessionID);
        return session;
    }

    default Long clientToLong(Client client) {
        return client != null ? client.getUserID() : null;
    }

    default Client longToClient(Long clientID) {
        if (clientID == null) return null;
        Client client = new Client();
        client.setUserID(clientID);
        return client;
    }
}