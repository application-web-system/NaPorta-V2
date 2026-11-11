package com.example.naporta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naporta.network.CreateRoomRequest
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class AdminUsersActivity : BaseAdminActivity() {

    private val adapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        val recycler = findViewById<RecyclerView>(R.id.recyclerUsers)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        findViewById<android.view.View>(R.id.btnNovoUsuario).setOnClickListener {
            showCreateUserDialog()
        }

        findViewById<android.view.View>(R.id.btnNovoQuarto)?.setOnClickListener {
            showCreateRoomDialog()
        }

        findViewById<android.view.View>(R.id.btnNovoHospede)?.setOnClickListener {
            startActivity(Intent(this, CreateGuestActivity::class.java))
        }

        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getUsers(SessionManager.adminBearer(this@AdminUsersActivity))
                if (response.isSuccessful) {
                    adapter.submitList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminUsersActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCreateUserDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_create_user, null)
        AlertDialog.Builder(this)
            .setTitle("Novo usuário")
            .setView(view)
            .setPositiveButton("Cadastrar") { _, _ ->
                val name = view.findViewById<EditText>(R.id.etDialogName).text.toString().trim()
                val email = view.findViewById<EditText>(R.id.etDialogEmail).text.toString().trim()
                val password = view.findViewById<EditText>(R.id.etDialogPassword).text.toString()
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                createUser(name, email, password)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun createUser(name: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createUser(
                    SessionManager.adminBearer(this@AdminUsersActivity),
                    com.example.naporta.network.CreateUserRequest(name, email, password)
                )
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminUsersActivity, "Usuário criado!", Toast.LENGTH_SHORT).show()
                    loadUsers()
                } else {
                    Toast.makeText(this@AdminUsersActivity, "Erro: e-mail já existe.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminUsersActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCreateRoomDialog() {
        val et = EditText(this).apply {
            hint = "Número do quarto (ex: 101)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setPadding(48, 32, 48, 32)
        }
        AlertDialog.Builder(this)
            .setTitle("Novo quarto")
            .setView(et)
            .setPositiveButton("Cadastrar") { _, _ ->
                val number = et.text.toString().trim()
                if (number.isEmpty()) return@setPositiveButton
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.api.createRoom(
                            SessionManager.adminBearer(this@AdminUsersActivity),
                            CreateRoomRequest(number)
                        )
                        if (response.isSuccessful) {
                            Toast.makeText(this@AdminUsersActivity, "Quarto ${response.body()?.number} criado!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AdminUsersActivity, "Quarto já existe.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@AdminUsersActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
