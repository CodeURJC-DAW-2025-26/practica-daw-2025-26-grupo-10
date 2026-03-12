package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.entities.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    
    ClientDTO toDTO(Client client);

    List <ClientDTO> toDTOs(Collection <Client> clients);

    @Mapping(target = "userID", ignore = true)
    Client toDomain(ClientDTO clientDTO);

}
