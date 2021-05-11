package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.KeyRemoveRequest
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.enum.Tipo
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.handler.exception.UsuarioNaoEncontradoException
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.repository.ChavePixRepository
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class ChavePixRemoveEndPointTest(val chavePixRepository: ChavePixRepository,
                                          val grpcClient: KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub){

    @Inject
    lateinit var itauErpClient: ItauErpClient


    @BeforeEach
    fun antesDeCada() {
        chavePixRepository.deleteAll()
    }



    @Test
    fun `nao deve remover uma chave do tipo random inexistente`(){

        //cenario
        val chave = "ae7daf87-3f17-4070-99fe-24fb399327b4"
        val idUsuario = "123456"

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.RANDOM)
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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.RANDOM)
        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.EMAIL)
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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.EMAIL)
        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.TELEFONE)
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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.TELEFONE)
        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.CPF)
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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.CPF)
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

        val chavePix = ChavePix(ContaEmbed("Itau", "123"), "123456", chave, Tipo.CONTA_CORRENTE, TipoChave.CPF)
        chavePixRepository.save(chavePix)

        //acao
        Mockito.`when`(itauErpClient.consultaClientes(idUsuario)).thenReturn(
            HttpResponse.ok()
        )


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


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub {
            return KeyManagerRemoveGrpc.newBlockingStub(channel)
        }
    }

}