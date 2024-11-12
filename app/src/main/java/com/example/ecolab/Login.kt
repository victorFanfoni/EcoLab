package com.example.ecolab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.edit_email)
        val password = findViewById<EditText>(R.id.edit_senha)
        val buttonEntrar = findViewById<Button>(R.id.buttonEntrar)
        val buttonCriarConta = findViewById<Button>(R.id.buttonCriarConta)
        val esqueceuSenha = findViewById<TextView>(R.id.textViewEsqueceuSenha)

        auth = Firebase.auth

        esqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueceuSenha::class.java))
        }

        buttonEntrar.setOnClickListener {
            val emailText = email.text.toString().trim()
            val senhaText = password.text.toString().trim()

            if (!isInputValid(emailText, senhaText)) return@setOnClickListener

             buttonEntrar.isEnabled = false
            loginUser(emailText, senhaText, buttonEntrar)
        }

        buttonCriarConta.setOnClickListener {
            navigateToCadastro()
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                Toast.makeText(this, "Por favor, preencha o campo de e-mail.", Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Digite um e-mail válido.", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Por favor, preencha o campo de senha.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun loginUser(email: String, password: String, button: Button) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                button.isEnabled = true
                if (task.isSuccessful) {
                    navigateToHome()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Erro na autenticação."
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun navigateToCadastro() {
        startActivity(Intent(this, Cadastro::class.java))
        finish()
    }
}
