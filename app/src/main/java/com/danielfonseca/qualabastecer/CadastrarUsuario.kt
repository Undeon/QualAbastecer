package com.danielfonseca.qualabastecer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class CadastrarUsuario : AppCompatActivity() {

    private val processoCadastro = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario)

        val cadastroNome= findViewById<View>(R.id.textoCadastroNome)
        val cadastroEmail = findViewById<View>(R.id.layoutCadastroEmail)
        val cadastroSenha = findViewById<View>(R.id.textoCadastroSenha)
        val cadastroConfirmaSenha = findViewById<EditText>(R.id.textoCadastroConfirmaSenha)

        val loginSenha = findViewById<EditText>(R.id.textoLoginSenha)
        val botaoCadastrar = findViewById<View>(R.id.botaoCadastroCadastrar)
        val emailLayout = findViewById<TextInputLayout>(R.id.layoutLoginEmail)
        val senhaLayout = findViewById<TextInputLayout>(R.id.layoutLoginSenha)

        botaoCadastrar.setOnClickListener{
            var email = cadastroEmail.toString()
            var senha = cadastroSenha.toString()
            var confirmaSenha = cadastroConfirmaSenha.text.toString()

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (senha.equals(confirmaSenha)) {
                    emailLayout.error = ""
                    processoCadastro.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var toast = Toast.makeText(this, "Cadastro efetuado com sucesso.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidCredentialsException) {
                                emailLayout.error = "Este usuário não existe."
                            }
                        }
                    }
                }
                else{
                    senhaLayout.error = "As senhas não conferem."
                }
            }
            else{
                emailLayout.error = "Favor verificar o e-mail fornecido"
            }
        }


    }
}