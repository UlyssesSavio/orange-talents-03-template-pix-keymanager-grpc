package br.com.zup.edu.model

import javax.persistence.Embeddable

@Embeddable
class ContaEmbed(val nome:String, val ispb:String)