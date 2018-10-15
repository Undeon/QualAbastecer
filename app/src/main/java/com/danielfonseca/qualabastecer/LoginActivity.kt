package com.danielfonseca.qualabastecer

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.youse.forms.rxform.IRxForm
import br.com.youse.forms.rxform.RxField
import br.com.youse.forms.rxform.RxForm
import br.com.youse.forms.validators.MinLengthValidator
import br.com.youse.forms.validators.RequiredValidator
import br.com.youse.forms.validators.Validator
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var form: IRxForm<Int>
    val disposables = CompositeDisposable()

    private val processoLogin = FirebaseAuth.getInstance()

    val emailValidators = listOf(
            RequiredValidator("O campo e-mail não pode ficar vazio."),
            EmailValidator("O e-mail digitado não é válido")
    )

    val passwordValidators = listOf(
            RequiredValidator("O campo senha não pode ficar vazio."),
            MinLengthValidator("A senha precisa ter pelo menos 8 caracteres", 8)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailChanges = textoLoginEmail.textChanges().map{ it.toString()}
        val passwordChanges = textoLoginSenha.textChanges().map{ it.toString()}

        val submitHappens = botaoLogar.clicks()

        val emailField = RxField(key = layoutLoginEmail.id,
                input = emailChanges,
                validators = emailValidators)

        val passwordField = RxField(key = layoutLoginSenha.id,
                input = passwordChanges,
                validators = passwordValidators)

        form = RxForm.Builder<Int>(submitHappens)
                .addField(emailField)
                .addField(passwordField)
                .build()


        disposables.add(form.onFieldValidationChange()
                .subscribe{
                findViewById<TextInputLayout>(it.first)
                    .error = it.second.firstOrNull()?.message
        })

        disposables.add(form.onFormValidationChange().subscribe{
            botaoLogar.isEnabled = it
        })

        disposables.add(form.onValidSubmit().subscribe{
            var email = textoLoginEmail.text.toString()
            var password = textoLoginSenha.text.toString()

            processoLogin.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    var toast = Toast.makeText(this, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show()
                }
            }
        })

        textoCriarConta.setOnClickListener{
            val intent = Intent(this, CadastrarUsuario::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        form.dispose()
        disposables.clear()
    }
}
