package com.danielfonseca.qualabastecer.vehicles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.api.CarsService
import com.danielfonseca.qualabastecer.model.ModelYear
import com.danielfonseca.qualabastecer.R
import com.danielfonseca.qualabastecer.model.Model
import com.danielfonseca.qualabastecer.model.ModelBrand
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*

class AddVehiclesActivity : AppCompatActivity() {

    var brandId = "-1"
    var modelId = "-1"

    lateinit var adapterModelBrand: ArrayAdapter<ModelBrand>
    lateinit var modelAdapter: ArrayAdapter<Model>
    lateinit var modelYearAdapter: ArrayAdapter<ModelYear>

    val retrofit = Retrofit.Builder()
            .baseUrl("https://fipeapi.appspot.com/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val carsService = retrofit.create(CarsService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        adapterModelBrand = ArrayAdapter(this@AddVehiclesActivity, android.R.layout.simple_spinner_item, mutableListOf(ModelBrand("-1", "Escolha o Fabricante")))
        modelAdapter = ArrayAdapter(this@AddVehiclesActivity, android.R.layout.simple_spinner_item, mutableListOf(Model("-1", "Escolha o Modelo")))
        modelYearAdapter = ArrayAdapter(this@AddVehiclesActivity, android.R.layout.simple_spinner_item, mutableListOf(ModelYear("-1", "Escolha o Ano Modelo")))

        popularSpinnerMarca()
        popularSpinnerModelo()
        popularSpinnerAno()

        getBrandList()

        checkBoxGas.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked || checkBoxEtanol.isChecked) {
                disableDiesel()
            }
            else{
                enableDiesel()
            }
        }

        checkBoxEtanol.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked || checkBoxGas.isChecked) {
                disableDiesel()
            }
            else{
                enableDiesel()
            }
        }

        checkBoxDiesel.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
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
        checkBoxGas.isEnabled = true
    }

    private fun disableOtto() {
        checkBoxEtanol.isChecked = false
        checkBoxEtanol.isEnabled = false
        checkBoxGas.isChecked = false
        checkBoxGas.isEnabled = false
    }

    fun getBrandList() {
        val call = carsService.getBrand()
        call.enqueue(object : Callback<List<ModelBrand>> {
            override fun onResponse(call: Call<List<ModelBrand>>?, response: Response<List<ModelBrand>>?) {
                if (response!!.isSuccessful) {
                    val brands = response.body()
                    adapterModelBrand.addAll(brands!!)
                }
            }

            override fun onFailure(call: Call<List<ModelBrand>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de marcas.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            getBrandList()
                        }
                        .show()
            }
        })
    }

    fun getModelList() {
        val call2 = carsService.getModel(brandId)
        call2.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>?, response2: Response<List<Model>>?) {
                if (response2!!.isSuccessful) {
                    val models = response2.body()
                    modelAdapter.addAll(models!!)
                }
            }

            override fun onFailure(call: Call<List<Model>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de modelos.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            getModelList()
                        }
                        .show()
            }
        })
    }

    fun getModelYearList() {
        val call3 = carsService.getModelYear(brandId, modelId)
        call3.enqueue(object : Callback<List<ModelYear>> {
            override fun onResponse(call: Call<List<ModelYear>>?, response3: Response<List<ModelYear>>?) {
                if (response3!!.isSuccessful) {
                    val modelYear = response3.body()
                    resetModelYearList()
                    modelYearAdapter.addAll(modelYear!!)
                }
            }

            override fun onFailure(call: Call<List<ModelYear>>?, t: Throwable?) {
                Snackbar.make(rootLayout, "Falha ao gerar a lista de ano modelo.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente") {
                            getModelYearList()
                        }
                        .show()
            }
        })
    }

    fun popularSpinnerMarca() {
        adapterModelBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownBrandList!!.adapter = adapterModelBrand
        dropDownBrandList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                resetModelList()
                resetModelYearList(
                )

                brandId = adapterModelBrand.getItem(position)!!.id

                if (brandId == "-1") {
                    dropDownBrandList.isEnabled = false
                    dropDownBrandYearList.isEnabled = false
                } else {
                    dropDownModel.isEnabled = true
                    dropDownBrandYearList.isEnabled = false
                    getModelList()
                }
            }
        }
    }

    fun popularSpinnerModelo() {
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownModel!!.adapter = modelAdapter
        dropDownModel?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                resetModelYearList()
                modelId = modelAdapter.getItem(position)!!.id

                if (modelId == "-1") {
                    dropDownBrandYearList.isEnabled = false
                } else {
                    dropDownBrandYearList.isEnabled = true
                    getModelYearList()
                }
            }
        }
    }

    fun popularSpinnerAno() {
        modelYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownBrandYearList!!.adapter = modelYearAdapter
    }

    fun resetModelYearList() {
        modelYearAdapter.clear()
        modelYearAdapter.add(ModelYear("-1", "Escolha o Ano de Fabricação"))
    }

    fun resetModelList() {
        modelAdapter.clear()
        modelAdapter.add(Model("-1", "Escolha o Modelo"))
    }
}