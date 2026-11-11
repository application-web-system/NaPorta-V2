package com.example.naporta

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.naporta.network.CreateGuestRequest
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class CreateGuestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_guest)

        val etName = findViewById<EditText>(R.id.etGuestName)
        val etRoomId = findViewById<EditText>(R.id.etRoomId)
        val btnCadastrar = findViewById<View>(R.id.btnCadastrarGuest)
        val layoutResult = findViewById<LinearLayout>(R.id.layoutTokenResult)
        val txtLink = findViewById<TextView>(R.id.txtGuestLink)
        val btnCopiar = findViewById<View>(R.id.btnCopiarLink)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        btnCadastrar.setOnClickListener {
            val name = etName.text.toString().trim()
            val roomId = etRoomId.text.toString().trim()
            if (name.isEmpty() || roomId.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnCadastrar.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.api.createGuest(
                        SessionManager.adminBearer(this@CreateGuestActivity),
                        CreateGuestRequest(name, roomId)
                    )
                    if (response.isSuccessful) {
                        val token = response.body()!!.token
                        val link = "naporta://guest/$token"
                        txtLink.text = link
                        layoutResult.visibility = View.VISIBLE
                        btnCopiar.setOnClickListener {
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("link_hospede", link))
                            Toast.makeText(this@CreateGuestActivity, "Link copiado!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@CreateGuestActivity, "Erro ao cadastrar hóspede.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CreateGuestActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
                } finally {
                    btnCadastrar.isEnabled = true
                }
            }
        }
    }
}
