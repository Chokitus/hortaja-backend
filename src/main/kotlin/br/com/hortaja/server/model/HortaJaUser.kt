package br.com.hortaja.server.model

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

  val name: String,

  @Indexed(unique = true, name = "idxEmail")
  val email: String,
  val password: String,

  val userType: UserType,

  @CreatedDate
  val createdAt: Instant = Instant.now(),

  @LastModifiedDate
  val updatedAt: Instant = Instant.now(),

  val favoritedFarms: Set<Farm> = setOf(),
  val ownedFarms: Set<Farm> = setOf()
) : GrantedAuthority {

  override fun equals(other: Any?): Boolean = other is HortaJaUser && other.id == this.id
  override fun getAuthority(): String = userType.name

}

enum class UserType {
  CONSUMER, PRODUCER, BOTH, ADMIN;
}
