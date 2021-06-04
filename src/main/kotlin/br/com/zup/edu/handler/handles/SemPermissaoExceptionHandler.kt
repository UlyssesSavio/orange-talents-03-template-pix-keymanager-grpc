package br.com.zup.edu.handler.handles

import io.grpc.Status
import javax.inject.Singleton
import br.com.zup.edu.handler.ExceptionHandler.StatusWithDetails
import br.com.zup.edu.handler.ExceptionHandler
import br.com.zup.edu.handler.exception.ChaveInvalidaException
import br.com.zup.edu.handler.exception.ChavePixExistenteException
import br.com.zup.edu.handler.exception.SemPermissaoException

@Singleton
class SemPermissaoExceptionHandler : ExceptionHandler<SemPermissaoException> {

    override fun handle(e: SemPermissaoException): StatusWithDetails {
        return StatusWithDetails(Status.PERMISSION_DENIED
            .withDescription(e.message)
            .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is SemPermissaoException
    }
}