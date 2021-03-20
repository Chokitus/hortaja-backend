package br.com.hortaja.server.dto

import br.com.hortaja.server.model.UserType

/**
 * TODO: Validate me
 *
 * @property name
 * @property username
 * @property password
 * @property type
 * @constructor Create empty User d t o
 */
data class UserDTO(
  val name: String,
  val username: String,
  val password: String,
  val type: UserType
)