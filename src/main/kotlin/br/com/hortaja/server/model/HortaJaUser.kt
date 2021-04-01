package br.com.hortaja.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import java.time.Instant
import java.util.*

@Document("user")
class HortaJaUser(
  @Id
  val id: UUID = UUID.randomUUID(),

  @Indexed(unique = true, name = "idxEmail")
  val email: String,

  @JsonIgnore
  val password: String,

  val userType: UserType,

  val userData: UserData,

  @CreatedDate
  val createdAt: Instant = Instant.now(),

  @LastModifiedDate
  val updatedAt: Instant = Instant.now(),
  ) : GrantedAuthority {

  override fun equals(other: Any?): Boolean = other is HortaJaUser && other.id == this.id
  override fun getAuthority(): String = userType.name

}

data class UserData(
  val name: String,
  val surname: String,
  var producerData: ProducerData? = null
)

data class ProducerData(
  val cnpj: String,
  val farmData: FarmData
)

data class FarmData(
  val farmName: String,
  val address: String,
  val cep: String,
  val schedule: String?,
  val phone: String,
  val description: String?,
  val photo: String?,
  val socialMedia: String
)

enum class UserType {
  CONSUMER, PRODUCER, BOTH, ADMIN;
}
