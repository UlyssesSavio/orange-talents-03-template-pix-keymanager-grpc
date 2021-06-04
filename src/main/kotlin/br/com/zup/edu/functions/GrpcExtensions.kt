package br.com.zup.edu.functions

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.request.*
import br.com.zup.edu.responseClient.ContasResponse
import br.com.zup.edu.sealed.Filtro
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import java.util.*
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

fun KeyPixRequest.toModel(contasResponse: ContasResponse): PixRequest {
    return PixRequest(
        contasResponse.instituicao.nome,
        BankAccount(contasResponse.instituicao.ispb, contasResponse.agencia, contasResponse.numero, tipoToAccont<tipo>(contasResponse.tipo)),
        Owner(PersonType.NATURAL_PERSON, contasResponse.titular.nome, contasResponse.titular.cpf),
        idConta = identificadorCliente,
         if(TipoChave.RANDOM==TipoChave.valueOf(tipoChave.name)) "" else chaveASerGerada
        , tipo = tipo
        , tipChave = TipoChave.valueOf(tipoChave.name)

    )


}

fun CarregaChavePixRequest.toModel(validator: Validator): Filtro {

    val filtro = when(filtroCase){
        CarregaChavePixRequest.FiltroCase.PIXID -> pixId.let {
            Filtro.PorPixId(clienteId = it.clientId, pixId = it.pixId)
        }
        CarregaChavePixRequest.FiltroCase.CHAVE -> Filtro.PorChave(chave)
        CarregaChavePixRequest.FiltroCase.FILTRO_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if(violations.isNotEmpty())
        throw ConstraintViolationException(violations)

    return filtro


}



inline fun <reified T : Enum<TypeKey>> typeToTipo(typeKey: TypeKey): tipoChave{
    return when(typeKey){
        TypeKey.CPF -> tipoChave.CPF
        TypeKey.PHONE -> tipoChave.TELEFONE
        TypeKey.EMAIL -> tipoChave.EMAIL
        TypeKey.RANDOM -> tipoChave.RANDOM
    }

}



inline fun <reified T : Enum<AccountBank>> accontToTipo(accont: AccountBank): tipo {
    return when(accont){
        AccountBank.CACC -> tipo.CONTA_CORRENTE
        AccountBank.SVGS -> tipo.CONTA_POUPANCA

    }

}

inline fun <reified T : Enum<tipo>> tipoToAccont(tip: tipo): AccountBank {
    return when(tip){
        tipo.CONTA_CORRENTE -> AccountBank.CACC
        tipo.CONTA_POUPANCA -> AccountBank.SVGS

        else -> AccountBank.CACC
    }

}