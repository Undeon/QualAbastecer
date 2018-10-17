package com.danielfonseca.qualabastecer.Veiculos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.danielfonseca.qualabastecer.R
import kotlinx.android.synthetic.main.activity_cadastrar_veiculos.*

class CadastrarVeiculosActivity : AppCompatActivity() {

    var marcaEscolhida = "Escolha o Fabricante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)

        dropDownModelo.isEnabled = false
        dropDownMarcas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                marcaEscolhida = dropDownMarcas.selectedItem.toString()

                if (marcaEscolhida == "Escolha o Fabricante" || marcaEscolhida == "---------------") {
                    dropDownModelo.isEnabled = false
                } else {
                    val dropModelos = ArrayAdapter.createFromResource(
                           this@CadastrarVeiculosActivity,
                            when (marcaEscolhida) {
                                "Audi" -> R.array.audi
                                "Chevrolet" -> R.array.chevrolet
                                "Fiat" -> R.array.fiat
                                "Ford" -> R.array.ford
                                "Honda" -> R.array.honda
                                "Hyundai" -> R.array.hyundai
                                "Jeep" -> R.array.jeep
                                "Mitsubishi" -> R.array.mitsubishi
                                "Nissan" -> R.array.nissan
                                "Peugeot" -> R.array.peugeot
                                "Renault" -> R.array.renault
                                "Toyota" -> R.array.toyota
                                "Volkswagen" -> R.array.volkswagen
                                "Acura" -> R.array.acura
                                "Agrale" -> R.array.agrale
                                "Alfa Romeo" -> R.array.alfaromeo
                                "Aston Martin" -> R.array.astonmartin
                                "Bentley" -> R.array.bentley
                                "BMW" -> R.array.bmw
                                "Bugatti" -> R.array.bugatti
                                "Buick" -> R.array.buick
                                "Cadillac" -> R.array.cadillac
                                "Changan" -> R.array.changan
                                "Chery" -> R.array.chery
                                "Chrysler" -> R.array.chrysler
                                "Citroën" -> R.array.citroen
                                "Dodge" -> R.array.dodge
                                "Effa" -> R.array.effa
                                "Ferrari" -> R.array.ferrari
                                "Geely" -> R.array.geely
                                "General Motors" -> R.array.generalmotors
                                "GMC" -> R.array.gmc
                                "Hafei" -> R.array.hafei
                                "Infinity" -> R.array.infinity
                                "Iveco" -> R.array.iveco
                                "Jac Motors" -> R.array.jacmotors
                                "Jaguar" -> R.array.jaguar
                                "Jinbei" -> R.array.jinbei
                                "Kia Motors" -> R.array.kiamotors
                                "Koenigsegg" -> R.array.koenigsegg
                                "Lamborghini" -> R.array.lamborghini
                                "Land Rover" -> R.array.landrover
                                "Lexus" -> R.array.lexus
                                "Lifan" -> R.array.lifan
                                "Mahindra" -> R.array.mahindra
                                "Maserati" -> R.array.maserati
                                "Mazda" -> R.array.mazda
                                "McLaren" -> R.array.mclaren
                                "Mercedes-Benz" -> R.array.mercedesbenz
                                "MG Motors" -> R.array.mgmotors
                                "Mini" -> R.array.mini
                                "Pagani" -> R.array.pagani
                                "Porsche" -> R.array.porsche
                                "Ram" -> R.array.ram
                                "Rolls Royce" -> R.array.rollsroyce
                                "Saab" -> R.array.saab
                                "Scania" -> R.array.scania
                                "Smart" -> R.array.smart
                                "Ssangyong" -> R.array.ssangyong
                                "Subaru" -> R.array.subaru
                                "Suzuki" -> R.array.suzuki
                                "Tata Motors" -> R.array.tatamotors
                                "Tesla" -> R.array.tesla
                                "Troller" -> R.array.troller
                                else -> R.array.volvo
                            },
                            android.R.layout.simple_spinner_item)
                    dropModelos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dropDownModelo.setAdapter(dropModelos)
                    dropDownModelo.isEnabled = true
                }
            }
        }
    }
}
