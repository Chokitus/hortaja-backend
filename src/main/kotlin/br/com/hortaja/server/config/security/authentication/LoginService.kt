package br.com.hortaja.server.config.security.authentication

import br.com.hortaja.server.repository.HortaJaUserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Service
class LoginService(
  private val hortaJaUserRepository: HortaJaUserRepository
) : ReactiveUserDetailsService {

  override fun findByUsername(username: String): Mono<UserDetails> =
    hortaJaUserRepository
      .findByEmail(username)
      .switchIfEmpty(BadCredentialsException("Invalid Credentials").toMono())
      .map { User(it.email, it.password, Collections.singletonList(it)) }

}