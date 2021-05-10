package br.com.zup.edu.client

import br.com.zup.edu.enum.Tipo
import br.com.zup.edu.responseClient.ClienteResponse
import br.com.zup.edu.responseClient.ContasResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1")
interface ItauErpClient {
    @Get("/clientes/{clienteId}/contas")
    fun consultaContas(@PathVariable clienteId:String, @QueryValue tipo:String) : HttpResponse<ContasResponse>

    @Get("/clientes/{clienteId}")
    fun consultaClientes(@PathVariable clienteId:String) : HttpResponse<ClienteResponse>
}