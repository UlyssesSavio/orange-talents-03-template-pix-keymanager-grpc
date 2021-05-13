package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.Instituicao
import br.com.zup.edu.model.Titular
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.*
import br.com.zup.edu.responseClient.ContasResponse
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
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ChavePixEndPointTest(
    val chavePixRepository: ChavePixRepository

) {

    @Inject
    lateinit var grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub

    @Inject
    lateinit var itauErpClient: ItauErpClient

    @Inject
    lateinit var bancoCentralClient: BancoCentralClient

    @BeforeEach
    fun antesDeCada() {
        chavePixRepository.deleteAll()
    }


    @Test
    fun `deve adicionar uma chave pix do tipo random`() {

        //cenario


        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val chaveRandom="sou aleatorio"

        val contasResponse = ContasResponse(
            tipo.CONTA_CORRENTE,
            Instituicao("itau", "123"),
            "456123",
            "12",
            Titular(idUsuario, "Rafael", cpf)
        )

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(contasResponse)
            )

        val createPixKeyResponse = CreatePixKeyResponse(TypeKey.RANDOM, chaveRandom, BankAccount("","","",AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON,"",""), LocalDateTime.now())

        Mockito.`when`(bancoCentralClient.cadastraChave(
            CreatePixKeyRequest.build(KeyPixRequest.newBuilder()
                .setChaveASerGerada("")
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.RANDOM)
                .setIdentificadorCliente(idUsuario)
                .build(),contasResponse )

        )).thenReturn(HttpResponse.created(createPixKeyResponse)
        )

        //acao
        val response = grpcClient.cadastra(
            KeyPixRequest.newBuilder()
                .setChaveASerGerada("")
                .setIdentificadorCliente(idUsuario)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.RANDOM)
                .build()
        )



        //validacao

        with(response) {
            assertNotNull(this.chavePix)
            assertEquals(chaveRandom, this.chavePix)

        }

    }


    @Test
    fun `deve dar email invalido`() {

        //cenario


        val email = "testeteste.com"
        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(
                    ContasResponse(
                        tipo.CONTA_CORRENTE,
                        Instituicao("itau", "123"),
                        "456123",
                        "12",
                        Titular(idUsuario, "Rafael", cpf)
                    )
                )
            )

        //acao
        assertThrows(StatusRuntimeException::class.java, {
            grpcClient.cadastra(
                KeyPixRequest.newBuilder()
                    .setChaveASerGerada(email)
                    .setIdentificadorCliente(idUsuario)
                    .setTipo(tipo.CONTA_CORRENTE)
                    .setTipoChave(tipoChave.TELEFONE)
                    .build()
            )
        }
        )


        //validacao


    }


    @Test
    fun `deve adicionar uma chave pix do tipo email`() {

        //cenario
        val email = "teste@teste.com"
        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val contasResponse = ContasResponse(
            tipo.CONTA_CORRENTE,
            Instituicao("itau", "123"),
            "456123",
            "12",
            Titular(idUsuario, "Rafael", cpf)
        )

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(contasResponse)
                )

        val createPixKeyResponse = CreatePixKeyResponse(TypeKey.EMAIL, email, BankAccount("","","",AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON,"",""), LocalDateTime.now())

        Mockito.`when`(bancoCentralClient.cadastraChave(
            CreatePixKeyRequest.build(KeyPixRequest.newBuilder()
                .setChaveASerGerada(email)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.EMAIL)
                .setIdentificadorCliente(idUsuario)
                .build(),contasResponse )

        )).thenReturn(HttpResponse.created(createPixKeyResponse)
        )

        //acao
        val response = grpcClient.cadastra(
            KeyPixRequest.newBuilder()
                .setChaveASerGerada(email)
                .setIdentificadorCliente(idUsuario)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.EMAIL)
                .build()
        )


        //validacao

        with(response) {
            assertNotNull(this.chavePix)
            assertEquals(this.chavePix, email)
        }

    }


    @Test
    fun `deve dar telefone invalido`() {

        //cenario


        val telefone = "935001234"
        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(
                    ContasResponse(
                        tipo.CONTA_CORRENTE,
                        Instituicao("itau", "123"),
                        "456123",
                        "12",
                        Titular(idUsuario, "Rafael", cpf)
                    )
                )
            )

        //acao
        assertThrows(StatusRuntimeException::class.java, {
            grpcClient.cadastra(
                KeyPixRequest.newBuilder()
                    .setChaveASerGerada(telefone)
                    .setIdentificadorCliente(idUsuario)
                    .setTipo(tipo.CONTA_CORRENTE)
                    .setTipoChave(tipoChave.TELEFONE)
                    .build()
            )
        }
        )


        //validacao


    }


    @Test
    fun `deve adicionar uma chave pix do tipo telefone`() {

        //cenario


        val telefone = "+5512935001234"
        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val contasResponse = ContasResponse(
            tipo.CONTA_CORRENTE,
            Instituicao("itau", "123"),
            "456123",
            "12",
            Titular(idUsuario, "Rafael", cpf)
        )

        val createPixKeyResponse = CreatePixKeyResponse(TypeKey.PHONE, telefone, BankAccount("","","",AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON,"",""), LocalDateTime.now())

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(contasResponse)
            )


        Mockito.`when`(bancoCentralClient.cadastraChave(
            CreatePixKeyRequest.build(KeyPixRequest.newBuilder()
                .setChaveASerGerada(telefone)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.TELEFONE)
                .setIdentificadorCliente(idUsuario)
                .build(),contasResponse )

        )).thenReturn(HttpResponse.created(createPixKeyResponse)
        )

        //acao
        val response = grpcClient.cadastra(
            KeyPixRequest.newBuilder()
                .setChaveASerGerada(telefone)
                .setIdentificadorCliente(idUsuario)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.TELEFONE)
                .build()
        )




        //validacao

        with(response) {
            assertNotNull(this.chavePix)
            assertEquals(this.chavePix, telefone)
        }

    }


    @Test
    fun `deve adicionar uma chave pix do tipo cpf`() {

        //cenario


        val cpf = "59715734030"
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"


        val contasResponse = ContasResponse(
            tipo.CONTA_CORRENTE,
            Instituicao("itau", "123"),
            "456123",
            "12",
            Titular(idUsuario, "Rafael", cpf)
        )

        val createPixKeyResponse = CreatePixKeyResponse(TypeKey.CPF, cpf, BankAccount("","","",AccountBank.CACC),
        Owner(PersonType.NATURAL_PERSON,"",""), LocalDateTime.now())


        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(contasResponse)
            )



        Mockito.`when`(bancoCentralClient.cadastraChave(
            CreatePixKeyRequest.build(KeyPixRequest.newBuilder()
                .setChaveASerGerada(cpf)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.CPF)
                .setIdentificadorCliente(idUsuario)
                .build(),contasResponse )

        )).thenReturn(HttpResponse.created(createPixKeyResponse)
        )


        //acao
        val response = grpcClient.cadastra(
            KeyPixRequest.newBuilder()
                .setChaveASerGerada(cpf)
                .setIdentificadorCliente(idUsuario)
                .setTipo(tipo.CONTA_CORRENTE)
                .setTipoChave(tipoChave.CPF)
                .build()
        )


        //validacao

        with(response) {
            assertNotNull(this.chavePix)
            assertEquals(this.chavePix, cpf)
        }

    }

    @Test
    fun `deve dar cpf invalido`() {

        //cenario
        val cpf = ""
        val idUsuario = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(
                HttpResponse.ok(
                    ContasResponse(
                        tipo.CONTA_CORRENTE,
                        Instituicao("itau", "123"),
                        "456123",
                        "12",
                        Titular(idUsuario, "Rafael", cpf)
                    )
                )
            )

        assertThrows(StatusRuntimeException::class.java, {
            grpcClient.cadastra(
                KeyPixRequest.newBuilder()
                    .setChaveASerGerada(cpf)
                    .setIdentificadorCliente(idUsuario)
                    .setTipo(tipo.CONTA_CORRENTE)
                    .setTipoChave(tipoChave.CPF)
                    .build()
            )
        }
        )
        //acao

        //validacao

    }

    @Test
    fun `deve dar usuario nao encontrado`() {

        //cenario



        val idUsuario = "nao existo"

        Mockito.`when`(itauErpClient.consultaContas(idUsuario, "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

        assertThrows(StatusRuntimeException::class.java, {
            grpcClient.cadastra(
                KeyPixRequest.newBuilder()
                    .setChaveASerGerada("")
                    .setIdentificadorCliente(idUsuario)
                    .setTipo(tipo.CONTA_CORRENTE)
                    .setTipoChave(tipoChave.RANDOM)
                    .build()
            )
        }
        )
        //acao

        //validacao

    }


    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }
        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T =  null as T
    }

    @MockBean(ItauErpClient::class)
    fun itauClient(): ItauErpClient? {
        return Mockito.mock(ItauErpClient::class.java)
    }

    @MockBean(BancoCentralClient::class)
    fun bancoCentralClient(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub? {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }

}