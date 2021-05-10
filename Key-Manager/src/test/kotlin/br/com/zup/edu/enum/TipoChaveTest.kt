package br.com.zup.edu.enum

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoChaveTest {

    @Nested
    inner class CPF{

        @Test
        fun `deve validar cpf com numero valido`(){
            with(TipoChave.CPF){
                Assertions.assertTrue(valida("59715734030"))
            }
        }

        @Test
        fun `nao deve validar cpf com numero invalido`(){
            with(TipoChave.CPF){
                Assertions.assertFalse(valida("59715734031"))
            }
        }

        @Test
        fun `nao deve validar com cpf nao informado`(){
            with(TipoChave.CPF){
                Assertions.assertFalse(valida(null))
            }
        }

        @Test
        fun `nao deve validar com cpf em branco`(){
            with(TipoChave.CPF){
                Assertions.assertFalse(valida(""))
            }
        }

    }


    @Nested
    inner class TELEFONE{

        @Test
        fun `deve validar com numero de telefone valido`(){
            with(TipoChave.TELEFONE){
                Assertions.assertTrue(valida("+5512935002222"))
            }
        }

        @Test
        fun `nao deve validar com numero de telefone invalido`(){
            with(TipoChave.TELEFONE){
                Assertions.assertFalse(valida("5512935002222"))
            }
        }

        @Test
        fun `nao deve validar com numero de telefone nao informado`(){
            with(TipoChave.TELEFONE){
                Assertions.assertFalse(valida(null))
            }
        }
        @Test
        fun `nao deve validar com numero de telefone em branco`(){
            with(TipoChave.TELEFONE){
                Assertions.assertFalse(valida(""))
            }
        }


    }

    @Nested
    inner class EMAIL{
        @Test
        fun `deve validar com email valido`(){
            with(TipoChave.EMAIL){
                Assertions.assertTrue(valida("teste@teste.com"))
            }
        }

        @Test
        fun `nao deve validar com email invalido`(){
            with(TipoChave.EMAIL){
                Assertions.assertFalse(valida("testeteste.com"))
            }
        }

        @Test
        fun `nao deve validar com email nao informado`(){
            with(TipoChave.EMAIL){
                Assertions.assertFalse(valida(null))
            }
        }

        @Test
        fun `nao deve validar com email em branco`(){
            with(TipoChave.EMAIL){
                Assertions.assertFalse(valida(""))
            }
        }


    }

    @Nested
    inner class RANDOM{
        @Test
        fun `deve validar com campo vazio`(){
            with(TipoChave.RANDOM){
                Assertions.assertTrue(valida(""))
            }
        }

        @Test
        fun `deve validar com campo nullo`(){
            with(TipoChave.RANDOM){
                Assertions.assertTrue(valida(null))
            }
        }

        @Test
        fun `nao deve validar com campo preenchido`(){
            with(TipoChave.RANDOM){
                Assertions.assertFalse(valida("a"))
            }
        }


    }

}