package com.danielfonseca.qualabastecer.API

import com.danielfonseca.qualabastecer.Model.Ano
import com.danielfonseca.qualabastecer.Model.Marca
import com.danielfonseca.qualabastecer.Model.Modelo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.firebase.firestore.auth.User
import okhttp3.ResponseBody
import retrofit2.http.Query


interface CarrosService {

    @GET("carros/marcas.json")
        fun recuperarMarca(): Call<List<Marca>>

    @GET("carros/veiculos/{idMarca}.json")
        fun recuperarModelo(@Path("idMarca") id: String): Call<List<Modelo>>

    @GET("carros/veiculo/{idMarca}/{idModelo}.json")
        fun recuperarAno(@Path("idMarca") idMarca: String, @Path("idModelo") userId: String): Call<List<Ano>>
}