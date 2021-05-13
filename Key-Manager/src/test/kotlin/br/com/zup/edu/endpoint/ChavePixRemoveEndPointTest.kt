package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyRemoveRequest
import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.handler.exception.UsuarioNaoEncontradoException
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.model.Instituicao
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.*
import br.com.zup.edu.responseClient.ClienteResponse
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class ChavePixRemoveEndPointTest(val chavePixRepository: ChavePixRepository
                                          ){
    @Inject
    lateinit var grpcClient: KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub

    @Inject
    lateinit var itauErpClient: ItauErpClient

    @Inject
    lateinit var bancoCentralClient:BancoCentralClient

    @BeforeEach
    fun antesDeCada() {
        chavePixRepository.deleteAll()
    }



    @Test
    fun `nao deve remover uma chave do tipo random inexistente`(){

        //cenario
        val chave = "ae7daf87-3f17-4070-99fe-24fb399327b4"
        val idUsuario = "123456"


        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
        Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.RANDOM
        )
        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


        //validacao

        assertThrows(StatusRuntimeException::class.java, { grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave("chaveErrada")
                .setIdUsuario(idUsuario)
                .build()
        )
        })

    }

    @Test
    fun `deve remover uma chave do tipo random`(){

        //cenario
        val chave = "ae7daf87-3f17-4070-99fe-24fb399327b4"
        val idUsuario = "123456"
        val ispb = "12345"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.RANDOM
        )
        chavePixRepository.save(chavePix)

        val clienteResponse = ClienteResponse("", "", "", Instituicao("itau", ispb))

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok(clienteResponse)
        )

        Mockito.`when`(bancoCentralClient.deletaChave(chave, DeletePixKeyRequest(chave, ispb))).thenReturn(HttpResponse.ok())


        //validacao

        val response = grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave(chave)
                .setIdUsuario(idUsuario)
                .build()
        )


        with(response){
            assertEquals(this.chave, chave)
            assertEquals(this.idUsuario, idUsuario)
            assertFalse(chavePixRepository.existsByChavePix(chave))
        }


    }


    @Test
    fun `nao deve remover uma chave do tipo email inexistente`(){

        //cenario
        val chave = "teste@teste.com"
        val idUsuario = "123456"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.EMAIL
        )


        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


        //validacao

        assertThrows(StatusRuntimeException::class.java, { grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave("chaveErrada")
                .setIdUsuario(idUsuario)
                .build()
        )
        })

    }


    @Test
    fun `deve remover uma chave do tipo email`(){

        //cenario
        val chave = "teste@teste"
        val idUsuario = "123456"
        val ispb = "12345"

        val chavePix = ChavePix("itau", BankAccount(ispb," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.EMAIL
        )

        chavePixRepository.save(chavePix)

        val clienteResponse = ClienteResponse("", "", "", Instituicao("itau", ispb))

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok(clienteResponse)
        )

        Mockito.`when`(bancoCentralClient.deletaChave(chave, DeletePixKeyRequest(chave, ispb))).thenReturn(HttpResponse.ok())


        //validacao

        val response = grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave(chave)
                .setIdUsuario(idUsuario)
                .build()
        )


        with(response){
            assertEquals(this.chave, chave)
            assertEquals(this.idUsuario, idUsuario)
            assertFalse(chavePixRepository.existsByChavePix(chave))
        }


    }



    @Test
    fun `nao deve remover uma chave do tipo telefone inexistente`(){

        //cenario
        val chave = "+5512935001232"
        val idUsuario = "123456"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.TELEFONE
        )

        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


        //validacao

        assertThrows(StatusRuntimeException::class.java, { grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave("chaveErrada")
                .setIdUsuario(idUsuario)
                .build()
        )
        })

    }


    @Test
    fun `deve remover uma chave do tipo telefone`(){

        //cenario
        val chave = "+5512935001232"
        val idUsuario = "123456"
        val ispb = "12345"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.TELEFONE
        )

        chavePixRepository.save(chavePix)

        val clienteResponse = ClienteResponse("", "", "", Instituicao("itau", ispb))

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok(clienteResponse)
        )

        Mockito.`when`(bancoCentralClient.deletaChave(chave, DeletePixKeyRequest(chave, ispb))).thenReturn(HttpResponse.ok())

        //validacao

        val response = grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave(chave)
                .setIdUsuario(idUsuario)
                .build()
        )


        with(response){
            assertEquals(this.chave, chave)
            assertEquals(this.idUsuario, idUsuario)
            assertFalse(chavePixRepository.existsByChavePix(chave))
        }


    }



    @Test
    fun `nao deve remover uma chave do tipo cpf com usuario inexistente`(){

        //cenario
        val chave = "82917295066"
        val idUsuario = "123456"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.CPF
        )

        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenThrow(
            UsuarioNaoEncontradoException::class.java
        )



        //validacao

        assertThrows(StatusRuntimeException::class.java, { grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave(chave)
                .setIdUsuario(idUsuario)
                .build()
        )
        })





    }



    @Test
    fun `nao deve remover uma chave do tipo cpf inexistente`(){

        //cenario
        val chave = "82917295066"
        val idUsuario = "123456"

        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.CPF
        )

        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


        //validacao

        assertThrows(StatusRuntimeException::class.java, { grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave("chaveErrada")
                .setIdUsuario(idUsuario)
                .build()
        )
        })

    }



    @Test
    fun `deve remover uma chave do tipo cpf`(){

        //cenario
        val chave = "82917295066"
        val idUsuario = "123456"

        val ispb="12345"
        val chavePix = ChavePix("itau", BankAccount(" "," ", " ", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, " "," "),"123", chave, tipo.CONTA_CORRENTE, tipoChave.CPF
        )

        chavePixRepository.save(chavePix)

        val clienteResponse = ClienteResponse("", "", "", Instituicao("itau", ispb))

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok(clienteResponse)
        )

        Mockito.`when`(bancoCentralClient.deletaChave(chave, DeletePixKeyRequest(chave, ispb))).thenReturn(HttpResponse.ok())



        //validacao

        val response = grpcClient.remove(
            KeyRemoveRequest.newBuilder()
                .setChave(chave)
                .setIdUsuario(idUsuario)
                .build()
        )


        with(response){
            assertEquals(this.chave, chave)
            assertEquals(this.idUsuario, idUsuario)
            assertFalse(chavePixRepository.existsByChavePix(chave))
        }


    }



    @MockBean(ItauErpClient::class)
    fun itauClient(): ItauErpClient? {
        return Mockito.mock(ItauErpClient::class.java)
    }

    @MockBean(BancoCentralClient::class)
    fun bancoCentralClient(): BancoCentralClient?{
        return Mockito.mock(BancoCentralClient::class.java)
    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub {
            return KeyManagerRemoveGrpc.newBlockingStub(channel)
        }
    }

}