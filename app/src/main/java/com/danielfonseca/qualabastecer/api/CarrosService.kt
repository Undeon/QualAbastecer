package com.danielfonseca.qualabastecer.api

import com.danielfonseca.qualabastecer.model.Ano
import com.danielfonseca.qualabastecer.model.Marca
import com.danielfonseca.qualabastecer.model.Modelo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CarrosService {

    @GET("carros/marcas.json")
        fun recuperarMarca(): Call<List<Marca>>

    @GET("carros/veiculos/{idMarca}.json")
        fun recuperarModelo(@Path("idMarca") id: String): Call<List<Modelo>>

    @GET("carros/veiculo/{idMarca}/{idModelo}.json")
        fun recuperarAno(@Path("idMarca") idMarca: String, @Path("idModelo") userId: String): Call<List<Ano>>
}