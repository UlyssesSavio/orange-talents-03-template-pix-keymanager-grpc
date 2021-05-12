package br.com.zup.edu.responseClient

import br.com.zup.edu.model.Instituicao
import br.com.zup.edu.model.Titular
import br.com.zup.edu.tipo

class ContasResponse(val tipo: tipo,
                     val instituicao: Instituicao, val agencia:String, val numero:String, val titular: Titular) {
}