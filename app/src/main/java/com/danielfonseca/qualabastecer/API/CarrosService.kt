package com.danielfonseca.qualabastecer.API

import com.danielfonseca.qualabastecer.Model.Marca
import retrofit2.Call
import retrofit2.http.GET

interface CarrosService {

    @GET("carros/marcas.json")
    fun recuperarMarca(): Call <List<Marca>>


}