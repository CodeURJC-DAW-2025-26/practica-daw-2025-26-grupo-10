package es.tickethub.tickethub.security;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.User;
import es.tickethub.tickethub.repositories.UserRepository;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		List<GrantedAuthority> roles = new ArrayList<>();

		if (user.getAdmin() != null && user.getAdmin()) {
			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}else{
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

		return new org.springframework.security.core.userdetails.User(
            user.getEmail(), 
			user.getPassword(),
            roles);

	}
}