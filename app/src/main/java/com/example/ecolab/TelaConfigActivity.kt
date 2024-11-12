package com.example.ecolab

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var userNamer: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telaconfig)

        auth = FirebaseAuth.getInstance()
        userNamer = findViewById(R.id.Username)

        val buttonAtualizarEmailSenha = findViewById<Button>(R.id.button_update_email_senha)
        val buttonAtualizarNomeTelefone = findViewById<Button>(R.id.button_atuli_nome_telefone)
        val buttonDeletarConta = findViewById<Button>(R.id.buttonDeletarConta)
        val buttonHome = findViewById<LinearLayout>(R.id.Home)

        displayUserName()

        setupButtonListeners(
            buttonAtualizarEmailSenha,
            buttonAtualizarNomeTelefone,
            buttonDeletarConta,
            buttonHome
        )
    }

    private fun setupButtonListeners(
        buttonAtualizarEmailSenha: Button,
        buttonAtualizarNomeTelefone: Button,
        deleteButton: Button,
        homeButton: LinearLayout
    ) {
        buttonAtualizarEmailSenha.setOnClickListener {
            navigateToAndFinish(UpdateEmailAndPassword::class.java)
        }
        buttonAtualizarNomeTelefone.setOnClickListener {
            navigateToAndFinish(updateNameAndPhoneactivity::class.java)
        }
        deleteButton.setOnClickListener {
            deletarConta()
        }
        homeButton.setOnClickListener {
            navigateToAndFinish(HomeActivity::class.java)
        }
    }

    private fun displayUserName() {
        val user = auth.currentUser
        user?.uid?.let { userId ->
            db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    userNamer.text = document.getString("nome") ?: "Usuário"
                }
                .addOnFailureListener { e ->
                    userNamer.text = "Erro ao carregar nome: ${e.message}"
                }
        } ?: run {
            userNamer.text = "Usuário não autenticado"
        }
    }

    private fun deletarConta() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja excluir sua conta? Essa ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> realizarExclusaoConta() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun realizarExclusaoConta() {
        val progressDialog = showProgressDialog("Excluindo Conta", "Aguarde enquanto excluímos sua conta...")

        auth.currentUser?.let { user ->
            db.collection("Usuarios").document(user.uid).delete()
                .addOnSuccessListener {
                    user.delete().addOnCompleteListener { deleteTask ->
                        progressDialog.dismiss()
                        if (deleteTask.isSuccessful) {
                            showToast("Conta e informações deletadas com sucesso")
                            navigateToAndFinish(Login::class.java)
                        } else {
                            showToast("Erro ao deletar conta: ${deleteTask.exception?.message}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    showToast("Erro ao deletar informações do Firestore: ${e.message}")
                    Log.e("FirestoreDeletionError", "Erro ao deletar informações do usuário: ${e.message}")
                }
        } ?: showToast("Usuário não autenticado")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAndFinish(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }

    private fun showProgressDialog(title: String, message: String): ProgressDialog {
        return ProgressDialog(this).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            show()
        }
    }
}
