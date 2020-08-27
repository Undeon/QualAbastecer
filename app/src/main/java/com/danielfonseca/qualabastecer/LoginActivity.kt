package com.danielfonseca.qualabastecer

import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import br.com.youse.forms.rxform.IRxForm
import br.com.youse.forms.rxform.RxForm
import br.com.youse.forms.rxform.models.RxField
import br.com.youse.forms.validators.MinLengthValidator
import br.com.youse.forms.validators.RequiredValidator
import com.danielfonseca.qualabastecer.users.UserRegisterActivity
import com.danielfonseca.qualabastecer.validators.EmailValidator
import com.danielfonseca.qualabastecer.vehicles.AddVehiclesActivity
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

        val emailChanges    = textSignInEmail.textChanges().map{ it.toString()}
        val passwordChanges = textSignInPassword.textChanges().map{ it.toString()}
        val submitHappens   = buttonSignIn.clicks()

        val emailField      = RxField(key = layoutSignInEmail.id,
                                      input = emailChanges,
                                      validators = emailValidators)

        val passwordField   = RxField(key = textSignInPassword.id,
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
            buttonSignIn.isEnabled = it
        })

        disposables.add(form.onValidSubmit().subscribe{
            val email    = textSignInEmail.text.toString()
            val password = textSignInPassword.text.toString()

            processoLogin.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show()
                }
            }
        })

        textSignUp.setOnClickListener{
            val intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, AddVehiclesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        form.dispose()
        disposables.clear()
    }
}
