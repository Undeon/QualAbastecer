package com.danielfonseca.qualabastecer.Veiculos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.API.CarrosService
import com.danielfonseca.qualabastecer.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.danielfonseca.qualabastecer.Model.Marca
import com.danielfonseca.qualabastecer.Model.Modelo
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastrarVeiculosActivity : AppCompatActivity() {

    var marcaSelecionada = ""
    var modeloSelecionado = ""

    var listaMarca = mutableListOf<Marca>()

    var listaModelos = mutableListOf<Modelo>()

    var idSelecionado = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        listaMarca.add(0, Marca("-1", "Escolha o Fabricante"))

        val retrofit = Retrofit.Builder()
                .baseUrl("https://fipeapi.appspot.com/api/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val carroServiceMarca = retrofit.create(CarrosService::class.java)
        val call = carroServiceMarca.recuperarMarca()

        call.enqueue(object: Callback<List<Marca>>{
            override fun onResponse(call: Call<List<Marca>>?, response: Response<List<Marca>>?) {
                if(response!!.isSuccessful()){
                    val marcas = response.body()
                    marcas.toString()
                    listaMarca.addAll(marcas!!)

                    val aa = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, listaMarca)
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dropDownMarcas!!.setAdapter(aa)

                    dropDownMarcas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            idSelecionado = listaMarca[position].id

                            if(idSelecionado == "-1")
                            {
                                dropDownModelo.isEnabled = false
                            }
                            else {
                                marcaSelecionada = listaMarca[position].fipe_name

                                val retrofit = Retrofit.Builder()
                                        .baseUrl("https://fipeapi.appspot.com/api/1/carros/veiculos/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()

                                val carroServiceModelo = retrofit.create(CarrosService::class.java)
                                carroServiceModelo.enviaIdSelecionado(idSelecionado)
                                val call = carroServiceModelo.recuperarModelo()

                                call.enqueue(object : Callback<List<Modelo>> {
                                    override fun onResponse(call: Call<List<Modelo>>?, response: Response<List<Modelo>>?) {
                                        if (response!!.isSuccessful()) {
                                            val modelos = response.body()
                                            modelos.toString()
                                            listaModelos = modelos!!.toMutableList()

                                            val aa = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, listaModelos)
                                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                            dropDownModelo!!.setAdapter(aa)

                                            dropDownModelo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                                }

                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    modeloSelecionado = listaModelos[position].fipe_name
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<List<Modelo>>?, t: Throwable?) {
                                        call.toString()
                                    }
                                })
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Marca>>?, t: Throwable?) {
                call.toString()
            }
        })
    }
}
