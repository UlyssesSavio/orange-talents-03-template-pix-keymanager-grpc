package br.com.zup.edu.model

import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.request.AccountBank
import br.com.zup.edu.request.BankAccount
import br.com.zup.edu.request.Owner
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import org.hibernate.annotations.GenericGenerator
import org.hibernate.id.uuid.StandardRandomStrategy
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix(
    var instituicaoNome:String,
    @Embedded var bankAccount: BankAccount,
    @Embedded var owner: Owner,
    @field:NotBlank val idConta:String,
    @field:NotBlank @field:Column(unique=true) var chavePix:String,
    @field:Enumerated(EnumType.STRING)
    val tipo: tipo,
    @field:Enumerated(EnumType.STRING)
    val tipoChave: tipoChave
)
{
    @Id
    var id: String = UUID.randomUUID().toString()

    fun pertenteAo(idConta:String):Boolean = this.idConta == idConta

}