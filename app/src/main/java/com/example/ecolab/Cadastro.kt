package com.example.ecolab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Cadastro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var criarContaButton: Button
    private lateinit var voltarButton: Button

    companion object {
        private const val TAG = "CadastroActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        auth = Firebase.auth
        initializeViews()
        setButtonListeners()
    }

    private fun initializeViews() {
        emailEditText = findViewById(R.id.edit_email)
        passwordEditText = findViewById(R.id.edit_senha)
        confirmPasswordEditText = findViewById(R.id.edit_confirmarSenha)
        criarContaButton = findViewById(R.id.buttonCriarConta)
        voltarButton = findViewById(R.id.buttonVoltar)
    }

    private fun setButtonListeners() {
        criarContaButton.setOnClickListener { criarConta() }
        voltarButton.setOnClickListener { voltarParaLogin() }
    }

    private fun criarConta() {
        val emailText = emailEditText.text.toString()
        val senhaText = passwordEditText.text.toString()
        val confirmarSenhaText = confirmPasswordEditText.text.toString()

        if (!validarCampos(emailText, senhaText, confirmarSenhaText)) return

        criarContaButton.isEnabled = false
        auth.createUserWithEmailAndPassword(emailText, senhaText)
            .addOnCompleteListener(this) { task ->
                criarContaButton.isEnabled = true
                if (task.isSuccessful) {
                    Log.d(TAG, "Conta criada com sucesso")
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, InformacoesPessoais::class.java))
                    finish()
                } else {
                    handleSignUpError(task.exception)
                }
            }
    }

    private fun validarCampos(email: String, senha: String, confirmarSenha: String): Boolean {
        return when {
            email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty() -> {
                mostrarToast("Por favor, preencha todos os campos.")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                mostrarToast("Email inválido.")
                false
            }
            senha != confirmarSenha -> {
                mostrarToast("As senhas não coincidem.")
                false
            }
            senha.length < 6 -> {
                mostrarToast("A senha deve ter no mínimo 6 caracteres.")
                false
            }
            else -> true
        }
    }

    private fun handleSignUpError(exception: Exception?) {
        Log.w(TAG, "Falha na criação da conta", exception)
        val errorMessage = when (exception?.message) {
            "The email address is already in use by another account." -> "O e-mail já está em uso."
            "The password is invalid or the user does not have a password." -> "Senha inválida."
            else -> "Falha na criação da conta: ${exception?.localizedMessage}"
        }
        mostrarToast(errorMessage)
    }

    private fun mostrarToast(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    private fun voltarParaLogin() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}
