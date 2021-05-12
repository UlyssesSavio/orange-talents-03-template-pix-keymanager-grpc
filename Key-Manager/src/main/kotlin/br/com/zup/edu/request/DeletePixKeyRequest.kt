package br.com.zup.edu.request

import br.com.zup.edu.anotacao.OpenClass
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@OpenClass
data class DeletePixKeyRequest(@field:JsonProperty val key:String, @field:JsonProperty val participant:String)

@OpenClass
data class DeletePixKeyResponse(@field:JsonProperty val key:String, @field:JsonProperty val participant:String, @field:JsonProperty val deletedAt:LocalDateTime)