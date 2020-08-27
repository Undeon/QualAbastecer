package com.danielfonseca.qualabastecer.api

import com.danielfonseca.qualabastecer.model.Model
import com.danielfonseca.qualabastecer.model.ModelYear
import com.danielfonseca.qualabastecer.model.ModelBrand
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CarsService {

    @GET("carros/marcas.json")
        fun getBrand(): Call<List<ModelBrand>>

    @GET("carros/veiculos/{brandId}.json")
        fun getModel(@Path("brandId") id: String): Call<List<Model>>

    @GET("carros/veiculo/{brandId}/{modelId}.json")
        fun getModelYear(@Path("brandId") idBrand: String, @Path("modelId") userId: String): Call<List<ModelYear>>
}