package br.com.hortaja.server.service

import br.com.hortaja.server.config.google.GoogleConfig
import br.com.hortaja.server.model.ProducerData
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class GoogleMapsService(
	private val googleConfig: GoogleConfig
) {

	val client: WebClient = googleConfig.webClient()

	fun getDistanceMatrix(
		origin: String,
		destinations: List<ProducerData>
	) =
		client
			.get()
			.uri {
				it.queryParam("units", "metric")
					.queryParam("origins", origin)
					.queryParam("destinations", destinations.map { it.farmData.cep }.joinToString("|"))
					.queryParam("key", googleConfig.googleAPIKey)
					.build()
			}
			.retrieve()
			.bodyToMono<JsonNode>()
			.map { it["rows"][0]["elements"] }
			.map { data ->
				destinations.mapIndexed { index, producerData ->
					mapOf("farm" to producerData, "data" to data[index])
				}
			}

}
