package br.com.zup.edu.client

import br.com.zup.edu.request.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client


@Client("\${cliente.bcb}")
interface BancoCentralClient {

    @Post("/pix/keys", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun cadastraChave(@Body createPixKeyRequest: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("/pix/keys/{key}", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun deletaChave(@PathVariable key:String, @Body deletePixKeyRequest: DeletePixKeyRequest) : HttpResponse<DeletePixKeyResponse>

    @Get("/pix/keys/{key}", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun buscaPorChave(@PathVariable key: String) : HttpResponse<PixKeyDetailsResponse>
}