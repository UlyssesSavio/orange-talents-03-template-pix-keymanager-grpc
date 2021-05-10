package br.com.zup.edu.responseClient

import br.com.zup.edu.enum.Tipo
import br.com.zup.edu.model.Instituicao
import br.com.zup.edu.model.Titular
import javax.persistence.Embeddable
import javax.persistence.Embedded

class ContasResponse(val tipo:Tipo,
                     val instituicao: Instituicao, val agencia:String, val numero:String,  val titular: Titular) {
}