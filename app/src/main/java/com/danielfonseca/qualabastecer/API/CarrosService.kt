package com.danielfonseca.qualabastecer.API

import com.danielfonseca.qualabastecer.Model.Marcas
import retrofit2.Call
import retrofit2.http.GET

interface CarrosService {

    @GET("carros/marcas.json")
    fun recuperarMarcas(): Call <List<Marcas>>


}