package com.danielfonseca.qualabastecer.model

data class ModelYear(val id: String, val name: String) {

    override fun toString(): String {
        return name
    }
}