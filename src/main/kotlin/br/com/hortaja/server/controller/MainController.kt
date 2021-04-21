package br.com.hortaja.server.controller

import br.com.hortaja.server.handler.FarmHandler
import br.com.hortaja.server.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router

@Controller
class MainController(
	val userHandler: UserHandler,
	val farmHandler: FarmHandler
) {

	@Bean
	fun mainRouter() = router {
		POST("/signup") { userHandler.createUser(it) }

		"/user".nest {
			GET("/{id}") { userHandler.getUserById(it.pathVariable("id")) }
			GET("/cnpj/{cnpj}") { userHandler.getProducerByCNPJ(it.pathVariable("cnpj")) }
		}

		"/farm".nest {
			PUT("/{userId}") { farmHandler.updateUserFarm(it.pathVariable("userId"), it.bodyToMono()) }
			GET("/closest/{cep}") { farmHandler.getDistanceMatrix(it.pathVariable("cep")) }
		}
	}

}
