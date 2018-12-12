package com.danielfonseca.qualabastecer.veiculos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.api.CarrosService
import com.danielfonseca.qualabastecer.model.Ano
import com.danielfonseca.qualabastecer.R
import com.danielfonseca.qualabastecer.model.Marca
import com.danielfonseca.qualabastecer.model.Modelo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*

class CadastrarVeiculosActivity : AppCompatActivity() {

    var idMarca = "-1"
    var idModelo = "-1"

    lateinit var adapterMarca: ArrayAdapter<Marca>
    lateinit var adapterModelo: ArrayAdapter<Modelo>
    lateinit var adapterAno: ArrayAdapter<Ano>

    val retrofit = Retrofit.Builder()
            .baseUrl("https://fipeapi.appspot.com/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val carroService = retrofit.create(CarrosService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        adapterMarca = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, mutableListOf(Marca("-1", "Escolha o Fabricante")))
        adapterModelo = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, mutableListOf(Modelo("-1", "Escolha o Modelo")))
        adapterAno = ArrayAdapter(this@CadastrarVeiculosActivity, android.R.layout.simple_spinner_item, mutableListOf(Ano("-1", "Escolha o Ano Modelo")))

        popularSpinnerMarca()
        popularSpinnerModelo()
        popularSpinnerAno()

        recuperarMarca()

        checkBoxGasolina.setOnCheckedChangeListener { _, _ ->
            if(checkBoxGasolina.isChecked) {
                disableDiesel()
            }
            else{
                enableDiesel()
            }
        }

        checkBoxEtanol.setOnCheckedChangeListener { _, _ ->
            if(checkBoxEtanol.isChecked) {
                disableDiesel()
            }
            else{
                enableDiesel()
            }
        }

        checkBoxGNV.setOnCheckedChangeListener { _, _ ->
            if(checkBoxGNV.isChecked) {
                disableDiesel()
            }
            else{
                enableDiesel()
            }
        }

        checkBoxDiesel.setOnCheckedChangeListener { _, _ ->
            if(checkBoxDiesel.isChecked) {
                disableOtto()
            }
            else{
                enableOtto()
            }
        }
    }

    private fun enableDiesel() {
        checkBoxDiesel.isEnabled = true
    }

    private fun disableDiesel() {
        checkBoxDiesel.isChecked = false
        checkBoxDiesel.isEnabled = false
    }

    private fun enableOtto() {
        checkBoxEtanol.isEnabled = true
        checkBoxGNV.isEnabled = true
        checkBoxGasolina.isEnabled = true
    }

    private fun disableOtto() {
        checkBoxEtanol.isChecked = false
        checkBoxEtanol.isEnabled = false
        checkBoxGNV.isChecked = false
        checkBoxGNV.isEnabled = false
        checkBoxGasolina.isChecked = false
        checkBoxGasolina.isEnabled = false
    }

    fun recuperarMarca() {
        val call = carroService.recuperarMarca()
        call.enqueue(object : Callback<List<Marca>> {
            override fun onResponse(call: Call<List<Marca>>?, response: Response<List<Marca>>?) {
                if (response!!.isSuccessful) {
                    val marcas = response.body()
                    adapterMarca.addAll(marcas!!)
                }
            }

            override fun onFailure(call: Call<List<Marca>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de marcas.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            recuperarMarca()
                        }
                        .show()
            }
        })
    }

    fun recuperarModelo() {
        val call2 = carroService.recuperarModelo(idMarca)
        call2.enqueue(object : Callback<List<Modelo>> {
            override fun onResponse(call: Call<List<Modelo>>?, response2: Response<List<Modelo>>?) {
                if (response2!!.isSuccessful) {
                    val modelos = response2.body()
                    adapterModelo.addAll(modelos!!)
                }
            }

            override fun onFailure(call: Call<List<Modelo>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de modelos.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            recuperarModelo()
                        }
                        .show()
            }
        })
    }

    fun recuperarAno() {
        val call3 = carroService.recuperarAno(idMarca, idModelo)
        call3.enqueue(object : Callback<List<Ano>> {
            override fun onResponse(call: Call<List<Ano>>?, response3: Response<List<Ano>>?) {
                if (response3!!.isSuccessful) {
                    val ano = response3.body()
                    resetAno()
                    adapterAno.addAll(ano!!)
                }
            }

            override fun onFailure(call: Call<List<Ano>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de ano modelo.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            recuperarAno()
                        }
                        .show()
            }
        })
    }

    fun popularSpinnerMarca() {
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownMarcas!!.adapter = adapterMarca
        dropDownMarcas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                resetModelo()
                resetAno()

                idMarca = adapterMarca.getItem(position).id

                if (idMarca == "-1") {
                    dropDownModelo.isEnabled = false
                    dropDownAno.isEnabled = false
                } else {
                    dropDownModelo.isEnabled = true
                    dropDownAno.isEnabled = false
                    recuperarModelo()
                }
            }
        }
    }

    fun popularSpinnerModelo() {
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownModelo!!.adapter = adapterModelo
        dropDownModelo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                resetAno()
                idModelo = adapterModelo.getItem(position).id

                if (idModelo == "-1") {
                    dropDownAno.isEnabled = false
                } else {
                    dropDownAno.isEnabled = true
                    recuperarAno()
                }
            }
        }
    }

    fun popularSpinnerAno() {
        adapterAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownAno!!.adapter = adapterAno
    }

    fun resetAno() {
        adapterAno.clear()
        adapterAno.add(Ano("-1", "Escolha o Ano Modelo"))
    }

    fun resetModelo() {
        adapterModelo.clear()
        adapterModelo.add(Modelo("-1", "Escolha o Modelo"))
    }
}