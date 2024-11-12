package com.example.ecolab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InformacoesPessoais : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacao_pessoais)

         if (auth.currentUser == null) {
            redirectToLogin()
            return
        }

        val name = findViewById<EditText>(R.id.editTextTextName)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val buttonSalvar = findViewById<Button>(R.id.button)

        buttonSalvar.setOnClickListener {
            val nome = name.text.toString().trim()
            val telefone = phone.text.toString().trim()

            if (nome.isNotEmpty() && telefone.isNotEmpty()) {
                salvarInformacoes(nome, telefone)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun salvarInformacoes(nome: String, telefone: String) {
        val usuario = hashMapOf(
            "nome" to nome,
            "telefone" to telefone
        )

        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("Usuarios").document(userId).set(usuario)
                .addOnSuccessListener {
                    Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao salvar os dados: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
