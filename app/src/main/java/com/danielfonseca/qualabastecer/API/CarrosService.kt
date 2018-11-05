package com.danielfonseca.qualabastecer.API

import com.danielfonseca.qualabastecer.Model.Marca
import com.danielfonseca.qualabastecer.Model.Modelo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.firebase.firestore.auth.User




interface CarrosService {

    @GET("carros/marcas.json")
        fun recuperarMarca(): Call<List<Marca>>

    @GET("veiculos/{id}.json")
        fun enviaIdSelecionado(@Path("id") id: Int): Call<List<Marca>>

    @GET("veiculos/{id}.json")
        fun recuperarModelo(): Call<List<Modelo>>
}