package br.com.zup.edu.handler.handles

import io.grpc.Status
import javax.inject.Singleton
import br.com.zup.edu.handler.ExceptionHandler.StatusWithDetails
import br.com.zup.edu.handler.ExceptionHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChavePixExistenteException

@Singleton
class ChaveInvalidaExceptionHandler : ExceptionHandler<ChaveInvalidaException> {

    override fun handle(e: ChaveInvalidaException): StatusWithDetails {
        return StatusWithDetails(Status.INVALID_ARGUMENT
            .withDescription(e.message)
            .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ChaveInvalidaException
    }
}