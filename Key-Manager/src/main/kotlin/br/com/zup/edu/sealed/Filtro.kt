package br.com.zup.edu.sealed

import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.handler.exception.ChaveNaoEncontradaException
import br.com.zup.edu.model.ChavePixInfo
import br.com.zup.edu.repository.ChavePixRepository
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import java.lang.IllegalArgumentException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
sealed class Filtro {


    abstract fun filtra(chavePixRepository: ChavePixRepository, bancoCentralClient: BancoCentralClient): ChavePixInfo


    @Introspected
    data class PorPixId(
        @field:Valid @field:NotBlank val clienteId:String,
        @field:Valid @field:NotBlank val pixId:String
    ): Filtro(){


        override fun filtra(
            chavePixRepository: ChavePixRepository,
            bancoCentralClient: BancoCentralClient
        ): ChavePixInfo {
            return chavePixRepository.findById(pixId)
                .filter { it.pertenteAo(clienteId) }
                .map(ChavePixInfo::of)
                .orElseThrow {ChaveNaoEncontradaException("Chave nao encontrada")}

        }

    }


    @Introspected
    data class PorChave(@field:Valid @field:NotBlank @Size(max= 77) val chave: String):Filtro(){

        override fun filtra(
            chavePixRepository: ChavePixRepository,
            bancoCentralClient: BancoCentralClient
        ): ChavePixInfo {

            return chavePixRepository.findByChavePix(chave)
                .map(ChavePixInfo::of)
                .orElseGet {

                    val response = bancoCentralClient.buscaPorChave(chave)
                    when (response.status) {
                        HttpStatus.OK -> response.body()?.toChavePixInfo()
                        else -> throw ChaveNaoEncontradaException("Chave nao encontrada")
                    }
                }

        }
    }

    @Introspected
    class Invalido(): Filtro(){
        override fun filtra(
            chavePixRepository: ChavePixRepository,
            bancoCentralClient: BancoCentralClient
        ): ChavePixInfo {
            throw IllegalArgumentException("Chave Pix invalida ou nao informada")
        }
    }

}




