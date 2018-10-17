package com.danielfonseca.qualabastecer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.Toast
import br.com.youse.forms.rxform.IRxForm
import br.com.youse.forms.rxform.RxField
import br.com.youse.forms.rxform.RxForm
import br.com.youse.forms.validators.MinLengthValidator
import br.com.youse.forms.validators.RequiredValidator
import br.com.youse.forms.validators.Validator
import com.danielfonseca.qualabastecer.Validators.ComplexityValidator
import com.danielfonseca.qualabastecer.Validators.EmailValidator
import com.danielfonseca.qualabastecer.Validators.EqualsValidator
import com.danielfonseca.qualabastecer.Veiculos.CadastrarVeiculosActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_cadastrar_usuario.*

class CadastrarUsuarioActivity : AppCompatActivity() {

    lateinit var form: IRxForm<Int>
    val disposables = CompositeDisposable()

    val nomeValidators = listOf<Validator<String>>(
            RequiredValidator("O campo nome não pode ficar vazio.")
    )

    val emailValidators = listOf(
            RequiredValidator("O campo e-mail não pode ficar vazio."),
            EmailValidator("O e-mail digitado não é válido")
    )

    val passwordValidators = listOf(
            RequiredValidator("O campo senha não pode ficar vazio."),
            MinLengthValidator("A senha precisa ter pelo menos 8 caracteres", 8),
            ComplexityValidator("A senha precisa ter pelo menos um número e um caractere especial")
    )

    val passwordConfirmaValidators by lazy{ listOf(
            RequiredValidator("O campo senha não pode ficar vazio."),
            EqualsValidator("As senhas digitadas não são iguais. Favor verificar.", textoCadastroSenha)
    )}


    private val reference = FirebaseDatabase.getInstance().reference
    private val processoCadastro = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario)

        val usuarios = reference.child("usuarios")

        val nomeChanges = textoCadastroNome.textChanges().map{ it.toString()}
        val emailChanges = textoCadastroEmail.textChanges().map{ it.toString()}
        val passwordChanges = textoCadastroSenha.textChanges().map{ it.toString()}
        val passwordConfirmaChanges = textoCadastroConfirmaSenha.textChanges().map{ it.toString()}

        val submitHappens = botaoCadastroCadastrar.clicks()

        val nomeField = RxField(key = layoutCadastroNome.id,
                input = nomeChanges,
                validators = nomeValidators                )

        val emailField = RxField(key = layoutCadastroEmail.id,
                input = emailChanges,
                validators = emailValidators)

        val passwordField = RxField(key = layoutCadastroSenha.id,
                input = passwordChanges,
                validators = passwordValidators)

        val passwordConfirmaField = RxField(key = layoutCadastroConfirmaSenha.id,
                input = passwordConfirmaChanges,
                validators = passwordConfirmaValidators)

        form = RxForm.Builder<Int>(submitHappens)
                .addField(nomeField)
                .addField(emailField)
                .addField(passwordField)
                .addField(passwordConfirmaField)
                .build()


        disposables.add(form.onFieldValidationChange()
                .subscribe{
                    findViewById<TextInputLayout>(it.first)
                            .error = it.second.firstOrNull()?.message
                })

        disposables.add(form.onFormValidationChange().subscribe{
            botaoCadastroCadastrar.isEnabled = it
        })

        disposables.add(form.onValidSubmit().subscribe{

            botaoCadastroCadastrar.isEnabled = false

            var nome = textoCadastroNome.text.toString()
            var email = textoCadastroEmail.text.toString()
            var password = textoCadastroSenha.text.toString()

            val usuario = Usuario()
            usuario.nome = nome
            usuario.email = email

            processoCadastro.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    usuarios.push().setValue(usuario)
                    var toast = Toast.makeText(this, "Cadastro efetuado com sucesso.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, CadastrarVeiculosActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    botaoCadastroCadastrar.isEnabled = true
                    if(task.exception is FirebaseAuthException){
                        val errorCode = (task.exception as FirebaseAuthException).errorCode
                        if(errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                            var toast = Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
}