package br.com.hortaja.server.config.security

import br.com.hortaja.server.config.security.authentication.LoginService
import br.com.hortaja.server.config.security.authorization.JWTReactiveAuthorizationFilter
import br.com.hortaja.server.config.security.jwt.JWTService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

	companion object {
		val EXCLUDED_PATHS = arrayOf("/login", "/signup")
	}

	@Bean
	fun configureSecurity(
		http: ServerHttpSecurity,
		jwtAuthenticationFilter: AuthenticationWebFilter,
		jwtService: JWTService
	): SecurityWebFilterChain {
		return http
			.csrf().disable()
			.logout().disable()
			.authorizeExchange()
			.pathMatchers(*EXCLUDED_PATHS).permitAll()
			.pathMatchers("/admin/**").hasRole("ADMIN")
			.anyExchange().authenticated()
			.and()
			.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
			.addFilterAt(
				JWTReactiveAuthorizationFilter(jwtService),
				SecurityWebFiltersOrder.AUTHORIZATION
			)
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.build()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	fun authenticationWebFilter(
		reactiveAuthenticationManager: ReactiveAuthenticationManager,
		jwtConverter: ServerAuthenticationConverter,
		serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
		jwtServerAuthenticationFailureHandler: ServerAuthenticationFailureHandler
	): AuthenticationWebFilter =
		AuthenticationWebFilter(reactiveAuthenticationManager)
			.apply {
				setRequiresAuthenticationMatcher {
					ServerWebExchangeMatchers
						.pathMatchers(HttpMethod.POST, "/login", "/signin")
						.matches(it)
				}
				setServerAuthenticationConverter(jwtConverter)
				setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler)
				setAuthenticationFailureHandler(jwtServerAuthenticationFailureHandler)
				setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			}

	@Bean
	fun jacksonDecoder(): AbstractJackson2Decoder = Jackson2JsonDecoder()

	@Bean
	fun reactiveAuthenticationManager(
		loginService: LoginService,
		passwordEncoder: PasswordEncoder
	): ReactiveAuthenticationManager =
		UserDetailsRepositoryReactiveAuthenticationManager(loginService)
			.apply {
				setPasswordEncoder(passwordEncoder)
			}

}
