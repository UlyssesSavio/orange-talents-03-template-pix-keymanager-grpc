package br.com.zup.edu.model

import br.com.zup.edu.enum.Tipo
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.responseClient.ContasResponse
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix(

    @Embedded var contaEmbed: ContaEmbed,
    @field:NotBlank val idConta:String,
    @field:NotBlank @field:Column(unique=true)val chavePix:String,
    @field:Enumerated(EnumType.STRING)
    val tipo:Tipo,
    @field:Enumerated(EnumType.STRING)
    val tipoChave: TipoChave){
    @Id
    @GeneratedValue
    var id: Long? =null
}