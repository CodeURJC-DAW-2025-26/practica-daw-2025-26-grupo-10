package es.tickethub.tickethub.rest_controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.UserDTO;
import es.tickethub.tickethub.mappers.UserMapper;
import es.tickethub.tickethub.services.UserService;


@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public Collection <UserDTO> getUsers(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        
        return userMapper.toDTOs(userService.getUsers(page, size));
    }
    
}
