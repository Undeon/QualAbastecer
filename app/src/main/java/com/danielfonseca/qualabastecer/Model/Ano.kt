package com.danielfonseca.qualabastecer.Model

data class Ano(val id: String, val name: String) {

    override fun toString(): String {
        return name
    }
}