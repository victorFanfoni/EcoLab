package com.example.ecolab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdateEmailAndPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var buttonVoltar: ImageView
    private lateinit var editEmail: EditText
    private lateinit var editSenhaAtual: EditText
    private lateinit var editNewEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var confirmarPassword: EditText
    private lateinit var buttonAtualizar: Button
    private lateinit var buttonHome: LinearLayout
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_email_password)

        auth = FirebaseAuth.getInstance()

        buttonVoltar = findViewById(R.id.goback)
        editEmail = findViewById(R.id.edit_email)
        editSenhaAtual = findViewById(R.id.edit_senha_atual)
        editNewEmail = findViewById(R.id.edit_new_email)
        editSenha = findViewById(R.id.edit_senha)
        confirmarPassword = findViewById(R.id.edit_confirmarSenha)
        buttonAtualizar = findViewById(R.id.buttonSalvar)
        buttonHome = findViewById(R.id.Home)
        progressBar = findViewById(R.id.progressBar)

        buttonVoltar.setOnClickListener {
            navigateToAndFinish(TelaConfigActivity::class.java)
        }

        buttonAtualizar.setOnClickListener {
            val newEmail = editNewEmail.text.toString().trim()
            val newPassword = editSenha.text.toString().trim()
            val confirmPassword = confirmarPassword.text.toString().trim()
            val currentPassword = editSenhaAtual.text.toString().trim()

            if (validateInputs(newEmail, newPassword, confirmPassword, currentPassword)) {
                atualizarEmailSenha(newEmail, newPassword, currentPassword)
            }
        }

        buttonHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()
        }
    }

    private fun validateInputs(newEmail: String, newPassword: String, confirmPassword: String, currentPassword: String): Boolean {
        if (newEmail.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || currentPassword.isEmpty()) {
            showToast("Por favor, preencha todos os campos")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            showToast("Por favor, insira um e-mail válido")
            return false
        }
        if (newPassword.length < 6) {
            showToast("A senha deve ter pelo menos 6 caracteres")
            return false
        }
        if (newPassword != confirmPassword) {
            showToast("As senhas não coincidem")
            return false
        }
        if (newEmail == auth.currentUser?.email) {
            showToast("O novo e-mail deve ser diferente do atual")
            return false
        }
        return true
    }

    private fun atualizarEmailSenha(newEmail: String, newPassword: String, currentPassword: String) {
        progressBar.visibility = View.VISIBLE
        val user = auth.currentUser ?: return showToast("Usuário não autenticado").also { progressBar.visibility = View.GONE }

        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { emailVerificationTask ->
                    if (emailVerificationTask.isSuccessful) {
                         showToast("Verificação de e-mail enviada para o novo endereço. Verifique seu e-mail antes de continuar.")
                        clearFields()
                        showEmailVerificationDialog(newEmail)

                         user.updatePassword(newPassword).addOnCompleteListener { passwordUpdateTask ->
                            if (passwordUpdateTask.isSuccessful) {
                                showToast("Senha atualizada com sucesso!")
                            } else {
                                handleError("Erro ao atualizar a senha", passwordUpdateTask.exception)
                            }
                            progressBar.visibility = View.GONE
                        }
                    } else {
                        handleError("Erro ao enviar verificação de e-mail", emailVerificationTask.exception)
                        progressBar.visibility = View.GONE
                    }
                }
            } else {
                handleError("Reautenticação falhou", reauthTask.exception)
                progressBar.visibility = View.GONE
            }
        }
    }


    private fun showEmailVerificationDialog(newEmail: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirme seu novo e-mail")
            .setMessage("Verifique o e-mail que enviamos para $newEmail. Após confirmar, reabra a tela de configurações para continuar.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun handleError(message: String, exception: Exception?) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage("$message: ${exception?.localizedMessage}")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
        Log.e("UpdateError", "$message: ${exception?.localizedMessage}")
    }

    private fun showToast(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        editNewEmail.text.clear()
        editSenha.text.clear()
        confirmarPassword.text.clear()
        editSenhaAtual.text.clear()
    }

    private fun navigateToAndFinish(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }
}
