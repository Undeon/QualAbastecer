package com.danielfonseca.qualabastecer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

class CadastrarVeiculos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_veiculos)


        val dropDownMarcas = findViewById<Spinner>(R.id.dropDownMarcas)
        val dropDownModelos = findViewById<Spinner>(R.id.dropDownModelo)
        val cadastroAnoModelos = findViewById<EditText>(R.id.textoCadastroAnoModelo)

        val dropMarcas = ArrayAdapter.createFromResource(this,
                R.array.marcas, android.R.layout.simple_spinner_item)
        dropMarcas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dropDownMarcas.setAdapter(dropMarcas)

        dropDownMarcas.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val marcaEscolhida = dropMarcas.getItem(position)
            }
        }
    }
}
