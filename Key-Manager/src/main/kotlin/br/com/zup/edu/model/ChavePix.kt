package br.com.zup.edu.model

import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.tipo
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix(

    @Embedded var contaEmbed: ContaEmbed,
    @field:NotBlank val idConta:String,
    @field:NotBlank @field:Column(unique=true) var chavePix:String,
    @field:Enumerated(EnumType.STRING)
    val tipo: tipo,
    @field:Enumerated(EnumType.STRING)
    val tipoChave: TipoChave){
    @Id
    @GeneratedValue
    var id: Long? =null


}