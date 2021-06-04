package br.com.zup.edu.model

import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.request.BankAccount
import br.com.zup.edu.request.Owner
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import java.time.LocalDateTime
import java.util.*

@OpenClass
data class ChavePixInfo(val instituicaoNome:String,
                        val pixId: String? =null,
                        val clienteId:String?=null,
                        val tipo: tipoChave,
                        val chave:String,
                        val tipoDeConta: tipo,
                        val bankAccount: BankAccount,
                        val owner: Owner,
                        val registradaEm: LocalDateTime = LocalDateTime.now()
) {

    companion object{
        fun of(chave: ChavePix): ChavePixInfo{


            return ChavePixInfo(chave.instituicaoNome,
                chave.id,
                chave.idConta,
                chave.tipoChave,
                chave.chavePix,
                chave.tipo,
                chave.bankAccount,
                chave.owner,
                chave.registradaEm
                )

        }
    }

}