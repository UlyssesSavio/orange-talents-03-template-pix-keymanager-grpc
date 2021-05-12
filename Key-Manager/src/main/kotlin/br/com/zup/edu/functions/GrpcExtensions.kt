package br.com.zup.edu.functions

import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.request.PixRequest
import br.com.zup.edu.responseClient.ContasResponse
import java.util.*

fun KeyPixRequest.toModel(contasResponse: ContasResponse): PixRequest {
    return PixRequest(
        contaEmbed = ContaEmbed(contasResponse.instituicao.nome, contasResponse.instituicao.ispb),
        idConta = identificadorCliente,
         if(TipoChave.RANDOM==TipoChave.valueOf(tipoChave.name)) "" else chaveASerGerada
        , tipo = tipo
        , tipoChave = TipoChave.valueOf(tipoChave.name)
    )


}

