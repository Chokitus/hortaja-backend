package br.com.hortaja.server.service

import br.com.hortaja.server.config.google.GoogleConfig
import br.com.hortaja.server.model.ProducerData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class GoogleMapsService(
	val googleConfig: GoogleConfig
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
					.queryParam("destinations", destinations.joinToString("|"))
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

fun main() {
	val json =
		"""
			{
			   "destination_addresses" : [
			      "Aliança, Osasco - SP, 06236-180, Brasil",
			      "Jardim Nova Europa, Campinas - SP, 13044-380, Brasil"
			   ],
			   "origin_addresses" : [
			      "Av. Padre Arlindo Vieira - Vila Vermelha, São Paulo - SP, 04297-000, Brasil"
			   ],
			   "rows" : [
			      {
			         "elements" : [
			            {
			               "distance" : {
			                  "text" : "29,3 km",
			                  "value" : 29304
			               },
			               "duration" : {
			                  "text" : "41 minutos",
			                  "value" : 2448
			               },
			               "status" : "OK"
			            },
			            {
			               "distance" : {
			                  "text" : "101 km",
			                  "value" : 100967
			               },
			               "duration" : {
			                  "text" : "1 hora 28 minutos",
			                  "value" : 5289
			               },
			               "status" : "OK"
			            }
			         ]
			      }
			   ],
			   "status" : "OK"
			}
		""".trimIndent()

	val node = jacksonObjectMapper().readValue<JsonNode>(json)

	val message = node["rows"][0]["elements"][1]
	println(message)
	println(message["distance"]["value"].asInt())
	println(message["duration"]["value"].asInt())
}
