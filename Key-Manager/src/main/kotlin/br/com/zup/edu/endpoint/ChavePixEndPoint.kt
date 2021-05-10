package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.KeyPixResponse
import br.com.zup.edu.client.ItauErpClient
import br.com.zup.edu.functions.toModel
import br.com.zup.edu.handler.ErrorHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChavePixExistenteException
import br.com.zup.edu.handler.exception.UsuarioNaoEncontradoException
import br.com.zup.edu.model.ChavePix
import br.com.zup.edu.model.ContaEmbed
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.request.PixRequest
import br.com.zup.edu.responseClient.ContasResponse
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@ErrorHandler
@Validated
@Singleton
open class ChavePixEndPoint(@Inject val chavePixRepository: ChavePixRepository,
                            @Inject val itauErpClient: ItauErpClient): KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    open override fun cadastra(request: KeyPixRequest?, responseObserver: StreamObserver<KeyPixResponse>?) {


        if(chavePixRepository.existsByChavePix(request?.chaveASerGerada)){
            throw ChavePixExistenteException("Chave ja existente")
        }


        var consultaContas: HttpResponse<ContasResponse>? = null
        try {
            consultaContas = itauErpClient.consultaContas(request!!.identificadorCliente, request.tipo.toString())
        }catch (e: HttpClientResponseException){
            throw UsuarioNaoEncontradoException("Usuario nao encontrado")
        }
        val pixRequest : PixRequest = request.toModel(ContaEmbed(nome = consultaContas!!.body().instituicao.nome, ispb = consultaContas.body().instituicao.ispb))



        var chavePix: ChavePix? =null
        if (pixRequest.tipoChave.valida(pixRequest.chavePix)){
            chavePix = pixRequest.toModel()
            chavePixRepository.save(chavePix)
        }
        else throw ChaveInvalidaException("Chave ${pixRequest.tipoChave.name} invalida")


        responseObserver?.onNext(KeyPixResponse.newBuilder().setChavePix(chavePix.chavePix).build())
        responseObserver?.onCompleted()
    }








}