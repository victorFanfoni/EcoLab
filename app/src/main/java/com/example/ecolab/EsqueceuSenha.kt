package com.example.ecolab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class EsqueceuSenha : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceu_senha)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.edit_email)
        val buttonEnviar = findViewById<Button>(R.id.buttonEnviar)
        val buttonVoltar = findViewById<Button>(R.id.buttonVoltar)

        buttonEnviar.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (emailText.isEmpty()) {
                email.error = "Preencha o campo"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                email.error = "E-mail inválido"
            } else {
                enviarEmailRedefinicaoSenha(emailText)
            }
        }

        buttonVoltar.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun enviarEmailRedefinicaoSenha(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Falha ao enviar o e-mail: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
