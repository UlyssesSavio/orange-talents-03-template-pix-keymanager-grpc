package br.com.zup.edu.repository

import br.com.zup.edu.model.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, String>{
    fun existsByChavePix(chaveASerGerada: String?): Boolean
    fun deleteByChavePix(chave:String)
    fun findByChavePix(chave:String): Optional<ChavePix>
    fun findAllByIdConta(chave:String): MutableList<ChavePix>

}