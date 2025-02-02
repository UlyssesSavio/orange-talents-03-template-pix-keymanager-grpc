package br.com.zup.edu.request

import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.functions.accontToTipo

import br.com.zup.edu.functions.typeToTipo
import br.com.zup.edu.model.ChavePixInfo
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.responseClient.ContasResponse
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@OpenClass
data class CreatePixKeyRequest(
    @field:JsonProperty("keyType")
    @field:NotNull
    val keyType: TypeKey,
    @field:NotBlank
    @field:JsonProperty("key") val key: String,
    @field:JsonProperty("bankAccount") val bankAccount: BankAccount,
    @field:JsonProperty("owner") val owner: Owner
) {
    companion object {
        fun build(keyRequest: KeyPixRequest, account: ContasResponse): CreatePixKeyRequest {

            val owner = Owner(
                type = PersonType.NATURAL_PERSON,
                name = account.titular.nome, taxIdNumber = account.titular.cpf
            )

            val bankAccount = BankAccount(
                participant = account.instituicao.ispb, branch = account.agencia,
                accountNumber = account.numero, accountType = AccountBank.toBankType(account.tipo)
            )

            return CreatePixKeyRequest(TypeKey.toTypeKey(keyRequest.tipoChave), keyRequest.chaveASerGerada!!, bankAccount, owner)
        }
    }
}
@OpenClass
data class CreatePixKeyResponse(
    @field:JsonProperty val keyType: TypeKey,
    @field:JsonProperty val key: String,
    @field:JsonProperty val bankAccount: BankAccount,
    @field:JsonProperty val owner: Owner,
    @field:JsonProperty val createdAt: LocalDateTime
){

}

@OpenClass
data class PixKeyDetailsResponse(
    @field:JsonProperty val keyType: TypeKey,
    @field:JsonProperty val key: String,
    @field:JsonProperty val bankAccount: BankAccount,
    @field:JsonProperty val owner: Owner,
    @field:JsonProperty val createdAt: LocalDateTime
){

        fun toChavePixInfo(): ChavePixInfo{
           return  ChavePixInfo("",null,null,
               typeToTipo<TypeKey>(keyType),
               bankAccount=bankAccount,
               owner=owner,
               chave = key,
               tipoDeConta = accontToTipo<AccountBank>(bankAccount.accountType),
           )

        }

}

@Embeddable
@OpenClass
data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    @field:Enumerated(EnumType.STRING)
    val accountType: AccountBank
)
@Embeddable
@OpenClass
data class Owner(
    @field:Enumerated(EnumType.STRING)
    val type: PersonType,
    val name: String,
    val taxIdNumber: String
)
@OpenClass
enum class AccountBank {
    CACC, SVGS;

    companion object {
        fun toBankType(type: tipo): AccountBank {
            return when (type) {
                tipo.CONTA_CORRENTE -> CACC
                tipo.CONTA_POUPANCA -> SVGS
                else -> throw RuntimeException()
            }
        }
    }
}
@OpenClass
enum class PersonType {
    NATURAL_PERSON, LEGAL_PERSON;


}

@OpenClass
enum class TypeKey{
    CPF, PHONE, EMAIL, RANDOM;

    companion object{
        fun toTypeKey(tipo: tipoChave):TypeKey{
            return when(tipo){
                tipoChave.CPF -> CPF
                tipoChave.TELEFONE -> PHONE
                tipoChave.EMAIL -> EMAIL
                tipoChave.RANDOM -> RANDOM
                else -> throw RuntimeException()
            }

        }
    }
}