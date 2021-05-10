package br.com.zup.edu.handler.handles

import io.grpc.Status
import javax.inject.Singleton
import br.com.zup.edu.handler.ExceptionHandler.StatusWithDetails
import br.com.zup.edu.handler.ExceptionHandler
import br.com.zup.edu.handler.exception.ChavePixExistenteException

@Singleton
class ChavePixExistenteExceptionHandler : ExceptionHandler<ChavePixExistenteException> {

    override fun handle(e: ChavePixExistenteException): StatusWithDetails {
        return StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription(e.message)
            .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixExistenteException
    }
}