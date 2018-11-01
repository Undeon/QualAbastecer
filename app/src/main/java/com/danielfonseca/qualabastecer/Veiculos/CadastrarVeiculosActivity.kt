package com.danielfonseca.qualabastecer.Veiculos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.API.CarrosService
import com.danielfonseca.qualabastecer.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.danielfonseca.qualabastecer.Model.Marca
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastrarVeiculosActivity : AppCompatActivity() {

    var marcaEscolhida = "Escolha o Fabricante"

    var listaMarca = mutableListOf<Marca>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://fipeapi.appspot.com/api/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val carroService = retrofit.create(CarrosService::class.java)
        val call = carroService.recuperarMarca()

        call.enqueue(object: Callback<List<Marca>>{
            override fun onResponse(call: Call<List<Marca>>?, response: Response<List<Marca>>?) {
                if(response!!.isSuccessful()){
                    val marcas = response.body()
                    marcas.toString()
                    listaMarca = marcas!!.toMutableList()

                    val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMarca)
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dropDownMarcas!!.setAdapter(aa)
                }
            }

            override fun onFailure(call: Call<List<Marca>>?, t: Throwable?) {
                call.toString()
            }
        })



    }
}
