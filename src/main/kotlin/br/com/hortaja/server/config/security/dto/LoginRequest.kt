package br.com.hortaja.server.config.security.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LoginRequest(
	@field:Email(regexp = ".+@.+\\..+")
	@field:NotBlank
	val email: String,

	@field:NotBlank
	@field:Size(min = 4, max = 255)
	val password: String
)
