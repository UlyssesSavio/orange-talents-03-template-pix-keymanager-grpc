package br.com.zup.edu.responseClient

import java.time.LocalDateTime

class DeletePixKeyResponse(val key:String, val participant:String, val deletedAt: LocalDateTime ) {
}