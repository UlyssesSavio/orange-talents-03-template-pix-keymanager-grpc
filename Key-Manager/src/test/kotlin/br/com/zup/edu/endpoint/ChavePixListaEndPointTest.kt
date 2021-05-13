package br.com.zup.edu.endpoint

import br.com.zup.edu.*
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.AccountBank
import br.com.zup.edu.request.BankAccount
import br.com.zup.edu.request.Owner
import br.com.zup.edu.request.PersonType
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ChavePixListaEndPointTest(
    val chavePixRepository: ChavePixRepository
){

    @Inject
    lateinit var grpcClient: KeyManagerListaGrpc.KeyManagerListaBlockingStub


    @BeforeEach
    fun antesDeCada(){
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
    fun `deveria buscar chaves cadastradas`(){

        val response = grpcClient.lista(ListaChavesPixRequest.newBuilder().setClientId("123").build())

        with(response){
            assertEquals(this.chavesCount,4)

        }

    }

    @Test
    fun `deveria buscar nenhuma chave`(){

        val response = grpcClient.lista(ListaChavesPixRequest.newBuilder().setClientId("1").build())

        with(response){
            assertEquals(this.chavesCount,0)

        }

    }

    @Test
    fun `deveria dar exception de usuario invalido`(){

        assertThrows(StatusRuntimeException::class.java) {
            grpcClient.lista(ListaChavesPixRequest.newBuilder().setClientId("").build())
        }


    }


    private fun criaChave(chave:String, tipoChave: tipoChave): ChavePix {
        return ChavePix("Itau", BankAccount("123","123", "123", AccountBank.CACC),
            Owner(PersonType.NATURAL_PERSON, "Joao", "123"),"123",chave, tipo.CONTA_CORRENTE, tipoChave, LocalDateTime.now())

    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerListaGrpc.KeyManagerListaBlockingStub ?{
            return KeyManagerListaGrpc.newBlockingStub(channel)
        }
    }
}