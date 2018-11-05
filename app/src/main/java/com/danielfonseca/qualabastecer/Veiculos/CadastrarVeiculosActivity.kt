package com.danielfonseca.qualabastecer.Veiculos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.API.CarrosService
import com.danielfonseca.qualabastecer.Model.Ano
import com.danielfonseca.qualabastecer.R
import com.danielfonseca.qualabastecer.Model.Marca
import com.danielfonseca.qualabastecer.Model.Modelo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*

class CadastrarVeiculosActivity : AppCompatActivity() {

    var marcaSelecionada = ""
    var modeloSelecionado = ""
    var anoSelecionado = ""
    var listaMarca = mutableListOf<Marca>()
    var listaModelos = mutableListOf<Modelo>()
    var listaAno = mutableListOf<Ano>()
    var idMarca = "-1"
    var idModelo = "-1"

    val retrofit = Retrofit.Builder()
            .baseUrl("https://fipeapi.appspot.com/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val carroService = retrofit.create(CarrosService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        listaMarca.add(0, Marca("-1", "Escolha o Fabricante"))

        val call = carroService.recuperarMarca()
        call.enqueue(object: Callback<List<Marca>>{
            override fun onResponse(call: Call<List<Marca>>?, response: Response<List<Marca>>?) {
                if(response!!.isSuccessful()){
                    val marcas = response.body()
                    marcas.toString()
                    listaMarca.addAll(marcas!!)

                    adapterMarca()

                    dropDownMarcas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            listaModelos.clear()
                            listaModelos.add(0, Modelo("-1", "Escolha o Modelo"))
                            adapterModelo()
                            listaAno.clear()
                            listaAno.add(0, Ano("-1", "Escolha o Ano Modelo"))
                            adapterAno()

                            idMarca = listaMarca[position].id

                            if(idMarca == "-1")
                            {
                                dropDownModelo.isEnabled = false
                                dropDownAno.isEnabled = false
                            }
                            else {
                                dropDownModelo.isEnabled = true
                                marcaSelecionada = listaMarca[position].fipe_name
                                recuperarModelo()
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

    fun recuperarModelo(){
        val call2 = carroService.recuperarModelo(idMarca)
        call2.enqueue(object : Callback<List<Modelo>> {
            override fun onResponse(call: Call<List<Modelo>>?, response2: Response<List<Modelo>>?) {
                if (response2!!.isSuccessful()) {
                    val modelos = response2.body()
                    modelos.toString()
                    listaModelos.addAll(modelos!!)

                    adapterModelo()

                    dropDownModelo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            listaAno.clear()
                            listaAno.add(0, Ano("-1", "Escolha o Ano Modelo"))
                            adapterAno()

                            idModelo = listaModelos[position].id

                            if(idModelo == "-1")
                            {
                                dropDownAno.isEnabled = false
                            }
                            else {
                                modeloSelecionado = listaModelos[position].fipe_name
                                dropDownAno.isEnabled = true
                                recuperarAno()
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Modelo>>?, t: Throwable?) {
                call.toString()
            }
        })
    }

    fun recuperarAno(){
        val call3 = carroService.recuperarAno(idMarca, idModelo)
        call3.enqueue(object : Callback<List<Ano>> {
            override fun onResponse(call: Call<List<Ano>>?, response3: Response<List<Ano>>?) {
                if (response3!!.isSuccessful()) {
                    val ano = response3.body()
                    ano.toString()
                    listaAno.addAll(ano!!)

                    adapterAno()

                    dropDownAno?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            anoSelecionado = listaAno[position].name
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Ano>>?, t: Throwable?) {
                call.toString()
            }
        })
    }

    fun adapterMarca() {
        val adapterMarca = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, listaMarca)
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownMarcas!!.setAdapter(adapterMarca)
    }

    fun adapterModelo(){
        val adapterModelo = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, listaModelos)
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownModelo!!.setAdapter(adapterModelo)
    }

    fun adapterAno(){
        val adapterAno = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, listaAno)
        adapterAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownAno!!.setAdapter(adapterAno)
    }
}