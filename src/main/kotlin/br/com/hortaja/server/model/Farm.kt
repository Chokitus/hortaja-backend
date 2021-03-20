package br.com.hortaja.server.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Farm(
  @Id
  val id: UUID,
  val name: String
)