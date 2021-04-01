package br.com.hortaja.server.handler

import br.com.hortaja.server.dto.UserDTO
import br.com.hortaja.server.mapper.HortaUserMapper
import br.com.hortaja.server.repository.HortaJaUserRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserHandler(
  val hortaJaUserRepository: HortaJaUserRepository,
  val hortaUserMapper: HortaUserMapper,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun createUser(serverRequest: ServerRequest): Mono<ServerResponse> =
    serverRequest
      .bodyToMono<UserDTO>()
      .doOnNext { log.info("Requesting sign-up for user [{}]!", it.email) }
      .map { dto -> hortaUserMapper.fromDTO(dto) }
      .flatMap { hortaJaUserRepository.save(it) }
      .flatMap { ok().bodyValue(it) }
      .onErrorResume(DuplicateKeyException::class.java) { e ->
        log.warn("Duplicate sign-up attempt!", e)
        badRequest().bodyValue("Already exists!")
      }

  fun getUserById(id: String) =
    Mono
      .just(id)
      .map { UUID.fromString(it) }
      .doOnNext { log.info("Requesting user by id [{}]!", it) }
      .flatMap { hortaJaUserRepository.findById(it) }
      .flatMap { ok().bodyValue(it) }
      .switchIfEmpty(notFound().build())

  fun getProducerByCNPJ(cnpj: String) =
    hortaJaUserRepository
      .findByUserData_ProducerData_Cnpj(cnpj)
      .flatMap { ok().bodyValue(it) }
      .switchIfEmpty(notFound().build())

}