package br.com.hortaja.server.model

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Farm(
	@Id
	val id: UUID,
	val name: String
)
