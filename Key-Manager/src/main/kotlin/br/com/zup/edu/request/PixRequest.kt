package br.com.zup.edu.request

import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.tipo
import io.micronaut.validation.Validated
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.validation.constraints.NotBlank

@Validated
class PixRequest(@Embedded var contaEmbed: ContaEmbed, @field:NotBlank val idConta:String, @field:NotBlank @field:Column(unique=true)val chavePix:String, val tipo: tipo, val tipoChave: TipoChave){

    fun toModel():ChavePix{
        return ChavePix(contaEmbed,
            idConta,
            if(TipoChave.RANDOM==TipoChave.valueOf(tipoChave.name)) geraUUID() else chavePix,
            tipo,
            tipoChave)

    }

    fun geraUUID(): String{
        return UUID.randomUUID().toString()
    }
}