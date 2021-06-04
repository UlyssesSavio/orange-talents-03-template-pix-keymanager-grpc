package br.com.zup.edu.endpoint

import br.com.zup.edu.KeyManagerListaGrpc
import br.com.zup.edu.ListaChavesPixRequest
import br.com.zup.edu.ListaChavesPixResponse
import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.handler.ErrorHandler
import br.com.zup.edu.repository.ChavePixRepository
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@OpenClass
@Validated
@Singleton
class ChavePixListaEndPoint(@Inject val chavePixRepository: ChavePixRepository):KeyManagerListaGrpc.KeyManagerListaImplBase() {
    override fun lista(request: ListaChavesPixRequest, responseObserver: StreamObserver<ListaChavesPixResponse>) {

        if(request.clientId.isNullOrBlank())
            throw IllegalArgumentException("Id de cliente nao pode ser vazio")

        val map = chavePixRepository.findAllByIdConta(request.clientId).map {
            ListaChavesPixResponse.ChavePix.newBuilder()
                .setChave(it.chavePix)
                .setCriadaEm(
                    Timestamp.newBuilder()
                        .setNanos(it.registradaEm.nano)
                        .setSeconds(it.registradaEm.second.toLong())
                        .build()
                )
                .setPixId(it.id)
                .setTipoChave(it.tipoChave)
                .setTipoConta(it.tipo)
                .build()
        }

        responseObserver.onNext(ListaChavesPixResponse.newBuilder()
            .setClienteId(request.clientId)
            .addAllChaves(map)
            .build())
        responseObserver.onCompleted()

    }
}