package br.com.zup.edu.repository

import br.com.zup.edu.model.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, Long>{
    fun existsByChavePix(chaveASerGerada: String?): Boolean
    fun deleteByChavePix(chave:String)
}