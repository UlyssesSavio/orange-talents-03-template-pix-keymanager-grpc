package br.com.zup.edu.endpoint

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.CarregaChavePixResponse
import br.com.zup.edu.KeyManagerCarregaGrpc
import br.com.zup.edu.client.BancoCentralClient
import br.com.zup.edu.functions.toModel
import br.com.zup.edu.handler.ErrorHandler
import br.com.zup.edu.repository.ChavePixRepository
import br.com.zup.edu.util.CarregaChavePixResponseConverter
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.Validator

@ErrorHandler
@Singleton
class ChavePixCarregaEndPoint(@Inject private val chavePixRepository: ChavePixRepository,
                              @Inject private val bancoCentralClient: BancoCentralClient,
                              @Inject private val validator: Validator): KeyManagerCarregaGrpc.KeyManagerCarregaImplBase() {

    override fun carrega(request: CarregaChavePixRequest, responseObserver: StreamObserver<CarregaChavePixResponse>) {

        val filtro = request.toModel(validator)
        val chaveInfo = filtro.filtra(chavePixRepository, bancoCentralClient)

        responseObserver.onNext(CarregaChavePixResponseConverter().convert(chaveInfo))
        responseObserver.onCompleted()




    }
}