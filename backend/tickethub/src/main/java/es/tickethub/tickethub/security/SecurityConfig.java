package es.tickethub.tickethub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.tickethub.tickethub.security.jwt.JwtRequestFilter;
import es.tickethub.tickethub.security.jwt.JwtTokenProvider;
import es.tickethub.tickethub.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private RepositoryUserDetailsService userDetailsService;
	
	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    // Authentication provider that uses our custom UserDetailsService
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http
				.securityMatcher("/api/v1/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/api/v1/purchases/save").permitAll()
						.requestMatchers("/api/v1/clients/**", "/api/v1/purchases/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
						.anyRequest().permitAll());
		
		http.formLogin(formLogin -> formLogin.disable());

		http.csrf(csrf -> csrf.disable());

		http.httpBasic(httpBasic -> httpBasic.disable());

		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(new JwtRequestFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Define the security filter chain (routes, login, logout, roles)
	@Bean
	@Order(2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC Routes
						.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/public/**","/images/entities/**").permitAll()
						// PURCHASES AUTORIZED WITHOUT BEING LOGGED IN
						.requestMatchers(HttpMethod.POST, "/purchases/save").permitAll()
						// USER (CLIENT OR ADMIN) ROUTES
						.requestMatchers("/clients/**", "/purchases/**").hasAnyRole("USER", "ADMIN")
						// ONLY ADMIN ROUTES
						.requestMatchers("/admin/**").hasRole("ADMIN")
						// OTHER ROUTES
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/public/login")
						.loginProcessingUrl("/public/login")
						.failureUrl("/public/login?error=true")
						.usernameParameter("email")
						.passwordParameter("password")
						.defaultSuccessUrl("/public/selector", true)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/public/login")
						.permitAll());

		return http.build();
	}
}
