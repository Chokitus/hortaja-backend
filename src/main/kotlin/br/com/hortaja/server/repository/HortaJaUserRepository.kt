package br.com.hortaja.server.repository

import br.com.hortaja.server.model.HortaJaUser
import java.util.UUID
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface HortaJaUserRepository : ReactiveCrudRepository<HortaJaUser, UUID> {

	fun findByEmail(email: String): Mono<HortaJaUser>
	fun findByUserData_ProducerData_Cnpj(cnpj: String): Mono<HortaJaUser>

}
