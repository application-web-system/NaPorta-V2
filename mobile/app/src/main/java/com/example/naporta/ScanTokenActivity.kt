package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class ScanTokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_token)

        val etToken = findViewById<EditText>(R.id.etToken)
        val btnConfirm = findViewById<android.view.View>(R.id.btnConfirmToken)
        val btnCancel = findViewById<android.view.View>(R.id.btnCancel)

        btnCancel.setOnClickListener { finish() }

        btnConfirm.setOnClickListener {
            val token = etToken.text.toString().trim()
            if (token.isEmpty()) {
                Toast.makeText(this, "Cole o token do QRCode.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnConfirm.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.api.getGuestByToken(token)
                    if (response.isSuccessful) {
                        val guest = response.body()!!
                        SessionManager.saveGuest(this@ScanTokenActivity, guest.token, guest.id, guest.name, guest.room.number)
                        startActivity(
                            Intent(this@ScanTokenActivity, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
                    } else {
                        Toast.makeText(this@ScanTokenActivity, "Token inválido ou expirado.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ScanTokenActivity, "Sem conexão com o servidor.", Toast.LENGTH_LONG).show()
                } finally {
                    btnConfirm.isEnabled = true
                }
            }
        }
    }
}
