package es.tickethub.tickethub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC Routes
						.requestMatchers("/").permitAll()
						.requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/image/**").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/public/**","/images/entities/**").permitAll()
						// USER (CLIENT OR ADMIN) ROUTES
						.requestMatchers("/clients/**","/purchases/**").hasAnyRole("USER", "ADMIN")
                        //ONLY ADMIN ROUTES
						.requestMatchers("/removebook/*").hasRole("ADMIN")
                        // OTHER ROUTES
                        .anyRequest().authenticated()
                    )       
				.formLogin(formLogin -> formLogin
						.loginPage("/public/login")
                        .loginProcessingUrl("/public/login")//intercepta cualquier post que venga de la pantalla login
						.failureUrl("/public/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
						.defaultSuccessUrl("/clients/profile")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")//lo mismo que con el login, intercepta la señal de cerrar sesión
						.logoutSuccessUrl("/public/login")
						.permitAll());

		return http.build();
	}
}
