package br.com.hortaja.server.repository

import br.com.hortaja.server.model.HortaJaUser
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface HortaJaUserRepository : ReactiveCrudRepository<HortaJaUser, UUID> {

  fun findByEmail(email: String): Mono<HortaJaUser>

}