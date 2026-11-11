package com.example.naporta

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naporta.network.CreateMenuItemRequest
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class AdminMenuActivity : BaseAdminActivity() {

    private val adapter = MenuItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu)

        val recycler = findViewById<RecyclerView>(R.id.recyclerMenuItems)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        findViewById<android.view.View>(R.id.btnNewItem).setOnClickListener {
            showCreateMenuItemDialog()
        }

        loadMenuItems()
    }

    private fun loadMenuItems() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getMenuItems(SessionManager.adminBearer(this@AdminMenuActivity))
                if (response.isSuccessful) {
                    adapter.submitList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@AdminMenuActivity, "Erro ao carregar cardápio.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminMenuActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCreateMenuItemDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_create_menu_item, null)
        AlertDialog.Builder(this)
            .setTitle("Novo item")
            .setView(view)
            .setPositiveButton("Cadastrar") { _, _ ->
                val name = view.findViewById<EditText>(R.id.etMenuName).text.toString().trim()
                val desc = view.findViewById<EditText>(R.id.etMenuDesc).text.toString().trim()
                val price = view.findViewById<EditText>(R.id.etMenuPrice).text.toString().toDoubleOrNull()
                val imageUrl = view.findViewById<EditText>(R.id.etMenuImageUrl).text.toString().trim()
                val category = view.findViewById<EditText>(R.id.etMenuCategory).text.toString().trim()

                if (name.isEmpty() || price == null || category.isEmpty()) {
                    Toast.makeText(this, "Nome, preço e categoria são obrigatórios.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                createMenuItem(name, desc, price, imageUrl, category)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun createMenuItem(name: String, desc: String, price: Double, imageUrl: String, category: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createMenuItem(
                    SessionManager.adminBearer(this@AdminMenuActivity),
                    CreateMenuItemRequest(name, desc, price, imageUrl, category)
                )
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminMenuActivity, "Item criado!", Toast.LENGTH_SHORT).show()
                    loadMenuItems()
                } else {
                    Toast.makeText(this@AdminMenuActivity, "Erro ao criar item.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminMenuActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
