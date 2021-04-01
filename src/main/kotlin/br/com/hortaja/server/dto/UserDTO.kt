package br.com.hortaja.server.dto

import br.com.hortaja.server.model.UserType
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserDTO(
  @field:Email(regexp = ".+@.+\\..+")
  @field:NotBlank
  val email: String,

  val password: String,
  val type: UserType,
  val userData: UserDataDTO
)

data class UserDataDTO(
  val name: String,
  val surname: String,
  val producerData: ProducerDataDTO? = null
)

data class ProducerDataDTO(
  val cnpj: String,
  val farmName: String,
  val address: String,
  val cep: String,
  val schedule: String?,
  val phone: String,
  val description: String?,
  val photo: String?,
  val socialMedia: String
)