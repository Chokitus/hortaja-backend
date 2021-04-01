package br.com.hortaja.server.mapper

import br.com.hortaja.server.dto.ProducerDataDTO
import br.com.hortaja.server.dto.UserDTO
import br.com.hortaja.server.model.FarmData
import br.com.hortaja.server.model.HortaJaUser
import br.com.hortaja.server.model.ProducerData
import br.com.hortaja.server.model.UserData
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class HortaUserMapper(
  private val passwordEncoder: PasswordEncoder,
) {

  fun fromDTO(dto: UserDTO) =
    dto.run {
      HortaJaUser(
        email = email,
        password = passwordEncoder.encode(password),
        userType = type,
        userData = UserData(
          name = userData.name,
          surname = userData.surname,
          producerData = userData.producerData?.let { fromProducerData(it) }
        )
      )
    }

  fun fromProducerData(producerData: ProducerDataDTO): ProducerData =
    producerData.run {
      ProducerData(
        cnpj = cnpj,
        FarmData(
          farmName = farmName,
          address = address,
          cep = cep,
          schedule = schedule,
          phone = phone,
          description = description,
          photo = photo,
          socialMedia = socialMedia
        )
      )
    }
}