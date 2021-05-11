package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyPixResponse
import br.com.zup.edu.KeyRemoveRequest
import br.com.zup.edu.KeyRemoveResponse
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.handler.ErrorHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChaveNaoEncontradaException
import br.com.zup.edu.handler.exception.UsuarioNaoEncontradoException
import br.com.zup.edu.repository.ChavePixRepository
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton


@ErrorHandler
@Validated
@Singleton
class ChavePixRemoveEndPoint(@Inject val chavePixRepository: ChavePixRepository,
                             @Inject val itauErpClient: ItauErpClient
):KeyManagerRemoveGrpc.KeyManagerRemoveImplBase() {

    override fun remove(request: KeyRemoveRequest?, responseObserver: StreamObserver<KeyRemoveResponse>?) {

        if(!chavePixRepository.existsByChavePix(request?.chave)) throw ChaveNaoEncontradaException("Chave nao existe")

        try {
            val client = itauErpClient.consultaClientes(request!!.idUsuario)
        }catch (e: HttpClientResponseException){
            throw  UsuarioNaoEncontradoException("Usuario nao encontrado")
        }

        chavePixRepository.deleteByChavePix(request.chave)

        responseObserver?.onNext(KeyRemoveResponse.newBuilder().setChave(request.chave).setIdUsuario(request.idUsuario).build())
        responseObserver?.onCompleted()

    }

}