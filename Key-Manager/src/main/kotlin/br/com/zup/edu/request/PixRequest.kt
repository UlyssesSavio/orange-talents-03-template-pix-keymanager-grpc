package br.com.zup.edu.request

import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import io.micronaut.validation.Validated
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.validation.constraints.NotBlank

@Validated
class PixRequest(var instituicaoNome:String, @Embedded var bankAccount: BankAccount, @Embedded var owner: Owner, @field:NotBlank val idConta:String, @field:NotBlank @field:Column(unique=true)val chavePix:String, val tipo: tipo, val tipChave: TipoChave){

    fun toModel():ChavePix{
        return ChavePix(
            instituicaoNome,
            bankAccount,
            owner,
            idConta,
            if(TipoChave.RANDOM==TipoChave.valueOf(tipChave.name)) geraUUID() else chavePix,
            tipo,
            tipoChave.valueOf(tipChave.name)
        )

    }

    fun geraUUID(): String{
        return UUID.randomUUID().toString()
    }
}