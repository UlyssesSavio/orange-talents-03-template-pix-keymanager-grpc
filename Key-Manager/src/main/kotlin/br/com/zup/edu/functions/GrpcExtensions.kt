package br.com.zup.edu.functions

import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.enum.Tipo
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.request.PixRequest
import java.util.*

fun KeyPixRequest.toModel(contasResponse: ContaEmbed): PixRequest {
    return PixRequest(
        contaEmbed = contasResponse,
        idConta = identificadorCliente,
         if(TipoChave.RANDOM==TipoChave.valueOf(tipoChave.name)) "" else chaveASerGerada
        , tipo = Tipo.valueOf(tipo.name)
        , tipoChave = TipoChave.valueOf(tipoChave.name)
    )


}

