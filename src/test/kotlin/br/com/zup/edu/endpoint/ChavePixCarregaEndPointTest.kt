package br.com.zup.edu.endpoint

import br.com.zup.edu.*
import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.enum.TipoChave
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.validation.Validator

@MicronautTest(transactional = false)
internal class ChavePixCarregaEndPointTest(val chavePixRepository: ChavePixRepository){
    @Inject
    lateinit var grpcClient: KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub

    @Inject
    lateinit var itauErpClient: ItauErpClient

    @Inject
    lateinit var bancoCentralClient: BancoCentralClient

    @Inject
    lateinit var validator:Validator

    @BeforeEach
    fun antesDeCada() {

        chavePixRepository.save(criaChave("12345678912",tipoChave.CPF))
        chavePixRepository.save(criaChave("+5511147411474",tipoChave.TELEFONE))
        chavePixRepository.save(criaChave("teste@teste.com",tipoChave.EMAIL))
        chavePixRepository.save(criaChave("random",tipoChave.RANDOM))

    }

    @AfterEach
    fun depoisDeCada(){
        chavePixRepository.deleteAll()
    }


    @Test
    fun `nao deve carregar chave por pixId e cliente Id quando filtro invalido`(){

        val thrown = assertThrows<StatusRuntimeException>{
            grpcClient.carrega(CarregaChavePixRequest.newBuilder()
                .setPixId(CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                    .setPixId("")
                    .setClientId("")
                    .build())
                .build())
        }

        with(thrown){
            assertEquals(Status.NOT_FOUND.code, status.code)
        }


    }


    @Test
    fun `deve carregar chave por valor da chave quando registro nao exister localmente, mas existe no bcb`(){

        val email="novo@email.com"

        val bancoResponse = criaPixKeyDetailsResponse(email)
        Mockito.`when`(bancoCentralClient.buscaPorChave(email))
            .thenReturn(HttpResponse.ok(bancoResponse))

        val response=grpcClient.carrega(CarregaChavePixRequest.newBuilder()
            .setChave(email)
            .build())

        with(response){
            assertEquals("", this.pixId)
            assertEquals("", this.clienteId)
            assertEquals(bancoResponse.keyType.name, this.chave.tipo.name)
            assertEquals(bancoResponse.key, this.chave.chave)
        }

    }



    @Test
    fun `deve buscar uma chave pela chave`(){

        val chave = chavePixRepository.findByChavePix("12345678912").get()


        val response = grpcClient.carrega(CarregaChavePixRequest.newBuilder().setChave("12345678912").build())

        with(response){
            assertEquals(this.chave.chave, chave.chavePix)
            assertEquals(this.chave.tipo, chave.tipoChave)
        }

    }


    @Test
    fun `nao deve buscar uma chave por id usuario e pixId invalido`(){


        assertThrows(
            StatusRuntimeException::class.java
        ) {
            grpcClient.carrega(
                CarregaChavePixRequest.newBuilder()
                    .setPixId(
                        CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                            .setClientId("nao existo")
                            .setPixId("nao existo")
                            .build()
                    ).build())
        }

    }



    @Test
    fun `nao deve buscar uma chave por chave`(){


        assertThrows(
            StatusRuntimeException::class.java
        ) {
            grpcClient.carrega(
                CarregaChavePixRequest.newBuilder()
                    .setChave("nao existo")
                    .build())
        }

    }



    @Test
    fun `deve buscar uma chave por id usuario e pixId`(){


        val chave = chavePixRepository.findByChavePix("12345678912").get()


        val response = grpcClient.carrega(
            CarregaChavePixRequest.newBuilder()
                .setPixId(
                    CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClientId(chave.idConta)
                        .setPixId(chave.id)
                        .build()
                ).build()
        )

        with(response){
            assertEquals(this.chave.chave, chave.chavePix)
            assertEquals(this.clienteId, chave.idConta)
            assertEquals(this.chave.tipo, chave.tipoChave)
        }


    }



    private fun criaChave(chave:String, tipoChave: tipoChave):ChavePix{
        return ChavePix("Itau", BankAccount("123","123", "123",AccountBank.CACC),
        Owner(PersonType.NATURAL_PERSON, "Joao", "123"),"123",chave, tipo.CONTA_CORRENTE, tipoChave, LocalDateTime.now())

    }

    private fun criaPixKeyDetailsResponse(chave:String): PixKeyDetailsResponse{
        return PixKeyDetailsResponse(TypeKey.EMAIL, chave,BankAccount("123","123", "123",AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, "Joao", "123"), LocalDateTime.now())
    }



    @MockBean(ItauErpClient::class)
    fun itauClient(): ItauErpClient? {
        return Mockito.mock(ItauErpClient::class.java)
    }

    @MockBean(BancoCentralClient::class)
    fun bancoCentralClient(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }

    @MockBean
    fun validator(): Validator{
        return Mockito.mock(Validator::class.java)
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub {
            return KeyManagerCarregaGrpc.newBlockingStub(channel)
        }
    }


}