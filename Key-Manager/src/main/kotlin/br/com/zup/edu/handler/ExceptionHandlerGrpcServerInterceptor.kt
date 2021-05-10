package br.com.zup.edu.handler

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TIP1: This interceptor has been tested with gRPC-Java, maybe it doesn't work with gRPC-Kotlin
 * TIP2: I'm not sure if this interceptor works well with all kind of gRPC-flows, like client and/or server streaming
 * TIP3: I think that implementing this interceptor via AOP would be better because we don't have to worry about the gRPC life-cycle
 */
@Singleton
class ExceptionHandlerGrpcServerInterceptor(@Inject val resolver: ExceptionHandlerResolver) : MethodInterceptor<BindableService, Any?> {
    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        try{
            return context.proceed()
        }catch (e: Exception){
            val handler = resolver.resolve(e)
            val status = handler.handle(e)

            GrpcEndpointArguments(context).response()
                .onError(status.asRuntimeException())
            return null
        }
    }

    private class GrpcEndpointArguments(val context: MethodInvocationContext<BindableService, Any?>) {

        fun response(): StreamObserver<*> {
            return context.parameterValues[1] as StreamObserver<*>
        }

    }


}