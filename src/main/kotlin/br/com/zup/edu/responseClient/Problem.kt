package br.com.zup.edu.responseClient

class Problem(val type:String, val status:String, val title:String, val detail:String, val violations: Violations) {
}

class Violations(val field:String, val message:String)