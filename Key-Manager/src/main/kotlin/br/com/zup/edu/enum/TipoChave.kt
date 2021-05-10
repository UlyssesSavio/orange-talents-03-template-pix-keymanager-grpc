package br.com.zup.edu.enum

import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator


enum class TipoChave {

    CPF {
        override fun valida(@NotBlank @Size(max=77)  chave: String?): Boolean {
            if(chave.isNullOrBlank()) return false

            if(!chave.matches(regex = Regex("^[0-9]{11}\$")))  return false

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }



        }
    },
    TELEFONE {
        override fun valida(@NotBlank @Size(max=77) chave: String?): Boolean {
            if(chave.isNullOrBlank()) return false

            if(chave.matches(regex = Regex("^\\+[1-9][0-9]\\d{1,14}\$"))){
                return true
            }
            return false
        }
    },
    EMAIL {
        override fun valida(@NotBlank @Email chave: String?): Boolean {
           if(chave.isNullOrBlank()) return false

            return  EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    RANDOM {

        override fun valida(@NotBlank @Size(max=77)chave: String?) = chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?): Boolean
}