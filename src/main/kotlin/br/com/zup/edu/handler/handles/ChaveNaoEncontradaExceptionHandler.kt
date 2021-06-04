package br.com.zup.edu.handler.handles

import io.grpc.Status
import javax.inject.Singleton
import br.com.zup.edu.handler.ExceptionHandler.StatusWithDetails
import br.com.zup.edu.handler.ExceptionHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChaveNaoEncontradaException
import br.com.zup.edu.handler.exception.ChavePixExistenteException

@Singleton
class ChaveNaoEncontradaExceptionHandler : ExceptionHandler<ChaveNaoEncontradaException> {

    override fun handle(e: ChaveNaoEncontradaException): StatusWithDetails {
        return StatusWithDetails(Status.NOT_FOUND
            .withDescription(e.message)
            .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ChaveNaoEncontradaException
    }
}