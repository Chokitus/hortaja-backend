package br.com.hortaja.server.config.google

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GoogleConfig {

	@Value("\${application.google-api-key}")
	lateinit var googleAPIKey: String

	fun webClient() =
		WebClient.create("https://maps.googleapis.com/maps/api/distancematrix/json")
}
