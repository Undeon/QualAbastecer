package com.danielfonseca.qualabastecer.Model

data class Marca(val id: String, val fipe_name: String) {

    override fun toString(): String {
        return fipe_name
    }
}