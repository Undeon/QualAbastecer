package com.danielfonseca.qualabastecer.API

import com.danielfonseca.qualabastecer.Model.Marca
import com.danielfonseca.qualabastecer.Model.Modelo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CarrosService {

    @GET("carros/marcas.json")
        fun recuperarMarca(): Call <List<Marca>>

    @GET("{id}.json")
        fun enviaIdSelecionado(@Path("id") id: String): Call<List<Marca>>
        fun recuperarModelo(): Call <List<Modelo>>

}