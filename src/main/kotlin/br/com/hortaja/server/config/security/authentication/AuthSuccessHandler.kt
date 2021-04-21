package br.com.hortaja.server.config.security.authentication

import br.com.hortaja.server.config.security.jwt.JWTService
import java.time.Duration
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
class AuthSuccessHandler(
	private val jwtService: JWTService
) : ServerAuthenticationSuccessHandler {

	companion object {
		private val ACCESS_TIME = Duration.ofMinutes(60).toMillis().toInt()
		private val REFRESH_TIME = Duration.ofHours(8).toMillis().toInt()
	}

	override fun onAuthenticationSuccess(
		webFilterExchange: WebFilterExchange?,
		authentication: Authentication?
	): Mono<Void> = mono {
		val principal = authentication?.principal ?: throw ResponseStatusException(
			HttpStatus.UNAUTHORIZED,
			"Unauthorized"
		)

		when (principal) {
			is User -> {
				val roles = principal.authorities.map { it.authority }.toTypedArray()
				val accessToken = jwtService.accessToken(principal.username, ACCESS_TIME, roles)
				val refreshToken = jwtService.refreshToken(principal.username, REFRESH_TIME, roles)

				webFilterExchange?.run {
					exchange.response.headers.set("Authorization", accessToken)
					exchange.response.headers.set("JWT-Refresh-Token", refreshToken)
				}
			}
		}

		return@mono null
	}

}
