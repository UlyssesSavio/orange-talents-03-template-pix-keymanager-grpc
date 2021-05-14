package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyPixResponse
import br.com.zup.edu.KeyRemoveRequest
import br.com.zup.edu.KeyRemoveResponse
import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.handler.ErrorHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChaveNaoEncontradaException
import br.com.zup.edu.handler.exception.SemPermissaoException
import br.com.zup.edu.handler.exception.UsuarioNaoEncontradoException
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.DeletePixKeyRequest
import br.com.zup.edu.responseClient.ClienteResponse
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton


@ErrorHandler
@Validated
@Singleton
class ChavePixRemoveEndPoint(@Inject val chavePixRepository: ChavePixRepository,
                             @Inject val itauErpClient: ItauErpClient,
                             @Inject val bancoCentralClient: BancoCentralClient
):KeyManagerRemoveGrpc.KeyManagerRemoveImplBase() {

    override fun remove(request: KeyRemoveRequest?, responseObserver: StreamObserver<KeyRemoveResponse>?) {

        if(!chavePixRepository.existsByChavePix(request?.chave)) throw ChaveNaoEncontradaException("Chave nao existe")



        val client:ClienteResponse
        try {
            val clientRes = itauErpClient.consultaClientes(request!!.idUsuario)
            client=clientRes.body()
        }catch (e: Exception){

            throw  UsuarioNaoEncontradoException("Usuario nao encontrado")
        }

        try {
            if(!chavePixRepository.existsByChavePixAndIdConta(request.chave, request.idUsuario))
                throw SemPermissaoException("Sem permissao para remover chave")

            bancoCentralClient.deletaChave(
                request.chave,
                DeletePixKeyRequest(request.chave, client.instituicao.ispb)
            )
        }catch (e:HttpClientResponseException){



            if(e.status == HttpStatus.FORBIDDEN){
                throw SemPermissaoException("Sem permissao para remover chave")
            }else
                throw ChaveNaoEncontradaException("Chave nao existe")
        }

        chavePixRepository.deleteByChavePix(request.chave)

        responseObserver?.onNext(KeyRemoveResponse.newBuilder().setChave(request.chave).setIdUsuario(request.idUsuario).build())
        responseObserver?.onCompleted()

    }

}