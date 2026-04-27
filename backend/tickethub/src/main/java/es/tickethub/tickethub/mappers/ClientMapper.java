package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.dto.ClientUpdateDTO;
import es.tickethub.tickethub.entities.Client;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {

    ClientDTO toDTO(Client client);

    List<ClientDTO> toDTOs(Collection<Client> clients);

    @Mapping(target = "userID", ignore = true)
    Client toDomain(ClientDTO clientDTO);

    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "version", source = "version")
    void updateEntityFromDto(ClientUpdateDTO dto, @MappingTarget Client entity);
}