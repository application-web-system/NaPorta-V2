package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deepLinkToken = intent?.data?.lastPathSegment
        when {
            deepLinkToken != null -> fetchAndSaveGuest(deepLinkToken)
            SessionManager.getGuestToken(this) != null -> applyGuestToUi(
                name = SessionManager.getGuestName(this) ?: "",
                room = SessionManager.getRoomNumber(this) ?: ""
            )
            else -> applyGuestToUi("", "") // sem sessão: campos vazios
        }

        findViewById<View>(R.id.cardMenu)?.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        setupCard(R.id.cardOrders, "Meus pedidos", "Acompanhe seu histórico", R.drawable.ic_receipt) {
            startActivity(Intent(this, MyOrdersActivity::class.java))
        }
        setupCard(R.id.cardAdmin, "Área admin", "Acesso restrito", R.drawable.ic_shield) {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
        setupCard(R.id.cardProfile, "Meu perfil", "Informações pessoais", R.drawable.ic_profile) {
            startActivity(Intent(this, GuestProfileActivity::class.java))
        }
        setupCard(R.id.cardSupport, "Suporte", "Fale com a recepção", R.drawable.ic_support) { }

        findViewById<View>(R.id.btnScanQrCode)?.setOnClickListener {
            startActivity(Intent(this, ScanTokenActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza UI se voltou da ScanTokenActivity com sessão nova
        val token = SessionManager.getGuestToken(this)
        if (token != null) {
            applyGuestToUi(
                name = SessionManager.getGuestName(this) ?: "",
                room = SessionManager.getRoomNumber(this) ?: ""
            )
        }
    }

    private fun setupCard(includeId: Int, title: String, subtitle: String, iconRes: Int, onClick: () -> Unit) {
        val card = findViewById<View>(includeId) ?: return
        card.findViewById<TextView>(R.id.cardTitle)?.text = title
        card.findViewById<TextView>(R.id.cardSubtitle)?.text = subtitle
        card.findViewById<android.widget.ImageView>(R.id.cardIcon)?.setImageResource(iconRes)
        card.setOnClickListener { onClick() }
    }

    private fun fetchAndSaveGuest(token: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getGuestByToken(token)
                if (response.isSuccessful) {
                    val guest = response.body()!!
                    SessionManager.saveGuest(this@MainActivity, guest.token, guest.id, guest.name, guest.room.number)
                    applyGuestToUi(guest.name, guest.room.number)
                } else {
                    Toast.makeText(this@MainActivity, "QRCode inválido ou expirado.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Sem conexão com o servidor.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun applyGuestToUi(name: String, room: String) {
        findViewById<TextView>(R.id.txtGuestName)?.text = name
        findViewById<TextView>(R.id.txtRoomNumber)?.text = if (room.isNotEmpty()) "Quarto $room" else ""
        val header = findViewById<View>(R.id.includeHeader)
        header?.findViewById<TextView>(R.id.tvHeaderRoomNumber)?.text = room
    }
}
