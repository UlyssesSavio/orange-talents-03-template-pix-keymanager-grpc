package br.com.zup.edu.client

import br.com.zup.edu.request.CreatePixKeyRequest
import br.com.zup.edu.request.CreatePixKeyResponse
import br.com.zup.edu.request.DeletePixKeyRequest
import br.com.zup.edu.request.DeletePixKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client


@Client("http://localhost:8082/api/v1")
interface BancoCentralClient {

    @Post("/pix/keys", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun cadastraChave(@Body createPixKeyRequest: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("/pix/keys/{key}", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun deletaChave(@PathVariable key:String, @Body deletePixKeyRequest: DeletePixKeyRequest) : HttpResponse<DeletePixKeyResponse>
}