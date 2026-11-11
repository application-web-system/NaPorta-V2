package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.naporta.network.LoginRequest
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class AdminLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnEntrar = findViewById<View>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnEntrar.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.api.login(LoginRequest(email, password))
                    if (response.isSuccessful) {
                        val jwt = response.body()!!.token
                        SessionManager.saveAdminJwt(this@AdminLoginActivity, jwt)
                        Toast.makeText(this@AdminLoginActivity, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AdminLoginActivity, AdminDashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@AdminLoginActivity, "E-mail ou senha inválidos.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AdminLoginActivity, "Sem conexão com o servidor.", Toast.LENGTH_SHORT).show()
                } finally {
                    btnEntrar.isEnabled = true
                }
            }
        }
    }
}
