package com.example.ecolab

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaConfigActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var userName: TextView
    private lateinit var userPhone: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telaconfig)

        auth = FirebaseAuth.getInstance()
        userName = findViewById(R.id.Username)
        userPhone = findViewById(R.id.UserPhone)

        val buttonUpdateEmailPassword = findViewById<Button>(R.id.button_update_email_senha)
        val buttonUpdateNamePhone = findViewById<Button>(R.id.button_atuli_nome_telefone)
        val buttonDeleteAccount = findViewById<Button>(R.id.buttonDeletarConta)
        val homeButton = findViewById<LinearLayout>(R.id.Home)

        displayUserInfo()

        buttonUpdateEmailPassword.setOnClickListener {
            navigateToAndFinish(UpdateEmailAndPassword::class.java)
        }

        buttonUpdateNamePhone.setOnClickListener {
            navigateToAndFinish(updateNameAndPhoneactivity::class.java)
        }

        buttonDeleteAccount.setOnClickListener {
            deleteAccount()
        }

        homeButton.setOnClickListener {
            navigateToAndFinish(HomeActivity::class.java)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserInfo() {
        val user = auth.currentUser
        user?.uid?.let { userId ->
            db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    userName.text = document.getString("nome") ?: "Usuário"
                    userPhone.text = document.getString("telefone") ?: "Telefone não disponível"
                }
                .addOnFailureListener {
                    userName.text = "Erro ao carregar nome"
                    userPhone.text = "Erro ao carregar telefone"
                }
        } ?: run {
            userName.text = "Usuário não autenticado"
            userPhone.text = "Telefone não disponível"
        }
    }

    private fun deleteAccount() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja excluir sua conta? Essa ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> performAccountDeletion() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun performAccountDeletion() {
        val progressDialog = showProgressDialog("Excluindo Conta", "Aguarde enquanto excluímos sua conta...")

        auth.currentUser?.let { user ->
            db.collection("Usuarios").document(user.uid).delete()
                .addOnSuccessListener {
                    user.delete().addOnCompleteListener { deleteTask ->
                        progressDialog.dismiss()
                        if (deleteTask.isSuccessful) {
                            Toast.makeText(this, "Conta deletada com sucesso", Toast.LENGTH_SHORT).show()
                            navigateToAndFinish(Login::class.java)
                        } else {
                            Toast.makeText(this, "Erro ao deletar conta: ${deleteTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Erro ao deletar informações do Firestore", Toast.LENGTH_SHORT).show()
                }
        } ?: Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAndFinish(destination: Class<*>) {
        startActivity(Intent(this, destination))
        finish()
    }

    private fun showProgressDialog(title: String, message: String): ProgressDialog {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
        return progressDialog
    }
}
