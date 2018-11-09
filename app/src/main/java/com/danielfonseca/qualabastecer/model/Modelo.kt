package com.danielfonseca.qualabastecer.model

data class Modelo(val id: String, val fipe_name: String) {

    override fun toString(): String {
        return fipe_name
    }
}