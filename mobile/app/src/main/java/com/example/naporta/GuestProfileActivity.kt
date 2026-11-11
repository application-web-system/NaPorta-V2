package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class GuestProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_profile)

        val name = SessionManager.getGuestName(this) ?: ""
        val room = SessionManager.getRoomNumber(this) ?: ""
        val initials = name.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }

        // Avatar iniciais
        findViewById<TextView>(R.id.txtProfileInitials)?.text = initials.ifEmpty { "?" }

        // Nome e quarto no card de identificação
        findViewById<TextView>(R.id.txtProfileName)?.text = name
        findViewById<TextView>(R.id.txtProfileRoom)?.text = "Quarto $room"

        // Linha de nome nos dados pessoais
        findViewById<TextView>(R.id.txtProfileNameDetail)?.text = name

        // Botão voltar
        findViewById<android.view.View>(R.id.btnBack)?.setOnClickListener { finish() }

        // Sair do quarto
        findViewById<android.view.View>(R.id.btnLeaveRoom)?.setOnClickListener {
            SessionManager.clearGuest(this)
            startActivity(Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
    }
}
