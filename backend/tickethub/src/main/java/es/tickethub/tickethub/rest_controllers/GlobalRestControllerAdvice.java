package es.tickethub.tickethub.rest_controllers;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.UserDTO;
import es.tickethub.tickethub.mappers.UserMapper;
import es.tickethub.tickethub.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class GlobalRestControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("isLogged", false, "isAdmin", false));
        }

        return userService.findByEmail(principal.getName())
                .map(user -> {
                    UserDTO dto = userMapper.toDTO(user);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}