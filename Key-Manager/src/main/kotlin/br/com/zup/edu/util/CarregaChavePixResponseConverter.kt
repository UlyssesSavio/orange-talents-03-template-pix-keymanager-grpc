package br.com.zup.edu.util

import br.com.zup.edu.CarregaChavePixResponse
import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.model.ChavePixInfo
import br.com.zup.edu.tipoChave
import com.google.protobuf.Timestamp
import java.time.ZoneId

@OpenClass
class CarregaChavePixResponseConverter {

    fun convert(chavePixInfo: ChavePixInfo): CarregaChavePixResponse{
        return CarregaChavePixResponse.newBuilder()
            .setClienteId(chavePixInfo.clienteId?.toString() ?: "")
            .setPixId(chavePixInfo.pixId?.toString() ?: "")
            .setChave(CarregaChavePixResponse.ChavePix.newBuilder()
                .setTipo(chavePixInfo.tipo)
                .setChave(chavePixInfo.chave)
                .setConta(CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(chavePixInfo.tipoDeConta)
                    .setInstituicao(chavePixInfo.instituicaoNome)
                    .setNomeDoTitular(chavePixInfo.owner.name)
                    .setCpfDoTitular(chavePixInfo.owner.taxIdNumber)
                    .setAgencia(chavePixInfo.bankAccount.branch)
                    .setNumeroDaConta(chavePixInfo.bankAccount.accountNumber)
                    .build()
                )
                .setCriadaEm(chavePixInfo.registradaEm.let {
                    val createAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createAt.epochSecond)
                        .setNanos(createAt.nano)
                        .build()

                })
            )
            .build()
    }

}