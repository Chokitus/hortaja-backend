package br.com.hortaja.server.handler

import br.com.hortaja.server.dto.UserDTO
import br.com.hortaja.server.model.HortaJaUser
import br.com.hortaja.server.repository.HortaJaUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.security.MessageDigest

@Service
class UserHandler(
  val hortaJaUserRepository: HortaJaUserRepository,
  val passwordEncoder: PasswordEncoder
) {
  fun createUser(serverRequest: ServerRequest): Mono<ServerResponse> =
    serverRequest
      .bodyToMono<UserDTO>()
      .map {
        HortaJaUser(
          name = it.name,
          email = it.username,
          password = passwordEncoder.encode(it.password),
          userType = it.type
        )
      }
      .flatMap { hortaJaUserRepository.save(it) }
      .flatMap { ServerResponse.ok().bodyValue(it) }
}


private fun String.sha256(): String =
  MessageDigest
    .getInstance("SHA-256")
    .digest(this.toByteArray())
    .fold("") { str, it -> str + "%02x".format(it) }