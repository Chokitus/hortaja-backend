package br.com.hortaja.server.controller

import br.com.hortaja.server.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Controller
class UserController(
  val userHandler: UserHandler
) {

  @Bean
  fun route() = router {
    POST("/signup") {
      userHandler.createUser(it)
    }
  }

}