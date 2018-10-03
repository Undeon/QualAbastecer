package com.danielfonseca.qualabastecer

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginActivity : AppCompatActivity() {

    private val processoLogin = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEmail = findViewById<EditText>(R.id.textoLoginEmail)
        val loginSenha = findViewById<EditText>(R.id.textoLoginSenha)
        val botaoLogar = findViewById<View>(R.id.botaoLogar)
        val criarConta = findViewById<View>(R.id.textoCriarConta)
        val emailLayout = findViewById<TextInputLayout>(R.id.layoutLoginEmail)

        botaoLogar.setOnClickListener{
            var email = loginEmail.text.toString()
            var senha = loginSenha.text.toString()

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.error = ""
                processoLogin.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var toast = Toast.makeText(this, "Login efetuado com sucesso.", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val exception = task.exception
                        if(exception is FirebaseAuthInvalidCredentialsException){
                            emailLayout.error = "Este usuário não existe."
                        }
                    }
                }
            }
            else{
                emailLayout.error = "Favor verificar o e-mail fornecido"
            }
        }
        criarConta.setOnClickListener{
            val intent = Intent(this, CadastrarUsuario::class.java)
            startActivity(intent)
        }
    }
}
