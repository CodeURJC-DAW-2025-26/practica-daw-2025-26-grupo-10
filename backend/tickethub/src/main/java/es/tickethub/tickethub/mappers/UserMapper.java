package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.UserDTO;
import es.tickethub.tickethub.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(Collection <User> users);

    @Mapping(target = "version", ignore = true)
    User toDomain(UserDTO userDTO);
}
