package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.PurchaseDTO;
import es.tickethub.tickethub.dto.PurchaseBasicDTO;
import es.tickethub.tickethub.entities.Purchase;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseMapper {
    
    @Mapping(source = "client.userID", target = "clientId")
    PurchaseDTO toDTO(Purchase purchase);

    PurchaseBasicDTO toBasicDTO(Purchase purchase);

    @Mapping(source = "client.userID", target = "clientId")
    List<PurchaseDTO> toDTOs(Collection<Purchase> purchases);

    @Mapping(target = "purchaseID", ignore = true)
    Purchase toDomain(PurchaseDTO purchaseDTO);
}