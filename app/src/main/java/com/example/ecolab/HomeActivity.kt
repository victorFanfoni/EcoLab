package com.example.ecolab

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var buttonConfiguracoes: ImageView
    private lateinit var buttonSobre: TextView
    private lateinit var buttonObjetivo: TextView
    private lateinit var buttonSolucao: TextView
    private lateinit var buttonSair: TextView
    private lateinit var buttonHome: LinearLayout
    private lateinit var nameUser: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

         if (FirebaseAuth.getInstance().currentUser == null) {
            redirectToLogin()
        } else {
            initializeViews()
            setButtonListeners()
            displayUserName()
        }
    }

    private fun initializeViews() {
        buttonConfiguracoes = findViewById(R.id.buttonConfiguracoes)
        buttonSobre = findViewById(R.id.button_sobre)
        buttonObjetivo = findViewById(R.id.button_objetivo)
        buttonSolucao = findViewById(R.id.button_solucao)
        buttonSair = findViewById(R.id.button_Sair)
        buttonHome = findViewById(R.id.Home)
        nameUser = findViewById(R.id.nameUser)
    }

    private fun setButtonListeners() {
        buttonSair.setOnClickListener { logout() }
        buttonConfiguracoes.setOnClickListener { navigateTo(TelaConfigActivity::class.java) }
        buttonSobre.setOnClickListener { navigateTo(SobreActivity::class.java) }
        buttonObjetivo.setOnClickListener { navigateTo(ObjetivoActivity::class.java) }
        buttonSolucao.setOnClickListener { navigateTo(SolucaoActivity::class.java) }
        buttonHome.setOnClickListener { showAlreadyOnHomeToast() }
    }

    private fun displayUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            db.collection("Usuarios").document(user.uid).get()
                .addOnSuccessListener { document ->
                    nameUser.text = document.getString("nome") ?: "Usuário"
                }
                .addOnFailureListener {
                    nameUser.text = "Erro ao carregar nome do usuário"
                }
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun navigateTo(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun showAlreadyOnHomeToast() {
        Toast.makeText(this, "Você já está na tela Home", Toast.LENGTH_SHORT).show()
    }
}
