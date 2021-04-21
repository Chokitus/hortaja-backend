package br.com.hortaja.server.handler

import br.com.hortaja.server.dto.ProducerDataDTO
import br.com.hortaja.server.mapper.HortaUserMapper
import br.com.hortaja.server.model.ProducerData
import br.com.hortaja.server.repository.HortaJaUserRepository
import br.com.hortaja.server.service.GoogleMapsService
import java.util.UUID
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Service
class FarmHandler(
	private val hortaUserMapper: HortaUserMapper,
	private val hortaJaUserRepository: HortaJaUserRepository,

	private val googleMapsService: GoogleMapsService
) {

	fun updateUserFarm(userId: String, data: Mono<ProducerDataDTO>): Mono<ServerResponse> =
		data
			.map { hortaUserMapper.fromProducerData(it) }
			.flatMap { newProducerData: ProducerData ->
				mono {
					hortaJaUserRepository
						.findById(UUID.fromString(userId))
						.awaitFirst()
						.apply {
							userData.producerData = newProducerData
						}
				}
			}
			.flatMap { hortaJaUserRepository.save(it) }
			.flatMap { ok().bodyValue(it) }

	fun getDistanceMatrix(cep: String) =
		hortaJaUserRepository
			.findAll()
			.filter { it.userData.producerData != null }
			.map { it.userData.producerData!! }
			.collectList()
			.flatMap { googleMapsService.getDistanceMatrix(cep, it) }
			.flatMap { ok().bodyValue(it) }
}
