package com.example.ecolab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class updateNameAndPhoneactivity: AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var homeButton: LinearLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_user_name_phone)

        backButton = findViewById(R.id.goback)
        nameEditText = findViewById(R.id.edit_nome)
        phoneEditText = findViewById(R.id.edit_telefone)
        updateButton = findViewById(R.id.buttonSalvar)
        homeButton = findViewById(R.id.Home)

        firebaseAuth = FirebaseAuth.getInstance()

        backButton.setOnClickListener {
            navigateToAndFinish(TelaConfigActivity::class.java)
        }

        updateButton.setOnClickListener {
            updateNameAndPhone()
        }

        homeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun updateNameAndPhone() {
        val name = nameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty()) {
            showToast("Por favor, preencha todos os campos")
            return
        }

        if (!isPhoneValid(phone)) {
            showToast("Por favor, insira um número de telefone válido")
            return
        }

        val user = firebaseAuth.currentUser
        if (user != null) {
            val userId = user.uid
             db.collection("Usuarios").document(userId).update("nome", name, "telefone", phone)
                .addOnSuccessListener {
                    showToast("Informações atualizadas com sucesso")
                    clearFields()
                    finish()
                }
                .addOnFailureListener { e ->
                    showToast("Erro ao atualizar informações: ${e.message}")
                }
        } else {
            showToast("Usuário não autenticado")
        }
    }

    private fun isPhoneValid(phone: String): Boolean {
         val regex = "^\\+?[0-9]{10,15}$".toRegex()
        return regex.matches(phone)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        nameEditText.text.clear()
        phoneEditText.text.clear()
    }

    private fun navigateToAndFinish(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }
}