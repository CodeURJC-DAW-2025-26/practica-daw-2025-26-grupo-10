package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.AdminDTO;
import es.tickethub.tickethub.entities.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    
    AdminDTO toDTO(Admin admin);

    List <AdminDTO> toDTOs(Collection <Admin> admins);

    @Mapping(target = "userID", ignore = true)
    Admin toDomain(AdminDTO adminDTO);
}
