package com.example.naporta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naporta.network.MenuItemResponse
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class MenuActivity : BaseActivity() {

    private val PAGE_SIZE = 15

    private var allItems: List<MenuItemResponse> = emptyList()
    private var filteredItems: List<MenuItemResponse> = emptyList()
    private var currentPage = 1
    private var selectedCategory = "Todos"
    private var searchQuery = ""

    private lateinit var adapter: MenuGuestAdapter
    private lateinit var txtPageInfo: TextView
    private lateinit var btnPrev: TextView
    private lateinit var btnNext: TextView
    private lateinit var chipGroup: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        adapter = MenuGuestAdapter { item ->
            val price = item.price.toDoubleOrNull() ?: 0.0
            CartStore.add(CartItem(
                productId = item.id.hashCode().toLong(),
                menuItemId = item.id,
                name = item.name,
                price = price,
                imageUrl = item.image_url ?: ""
            ))
            Toast.makeText(this, "${item.name} adicionado ao carrinho.", Toast.LENGTH_SHORT).show()
        }

        val recycler = findViewById<RecyclerView>(R.id.recyclerMenu)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        txtPageInfo = findViewById(R.id.txtPageInfo)
        btnPrev = findViewById(R.id.btnPrevPage)
        btnNext = findViewById(R.id.btnNextPage)
        chipGroup = findViewById(R.id.chipGroup)

        btnPrev.setOnClickListener {
            if (currentPage > 1) { currentPage--; renderPage() }
        }
        btnNext.setOnClickListener {
            val totalPages = totalPages()
            if (currentPage < totalPages) { currentPage++; renderPage() }
        }

        findViewById<EditText>(R.id.etSearch).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString()?.trim() ?: ""
                currentPage = 1
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loadMenu()
    }

    private fun loadMenu() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getMenuGuest()
                if (response.isSuccessful) {
                    allItems = response.body() ?: emptyList()
                    if (allItems.isEmpty()) {
                        Toast.makeText(this@MenuActivity, "Nenhum item disponível no momento.", Toast.LENGTH_SHORT).show()
                    }
                    buildCategoryChips()
                    applyFilters()
                } else {
                    Toast.makeText(this@MenuActivity, "Erro ao carregar cardápio.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MenuActivity, "Sem conexão com o servidor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buildCategoryChips() {
        val categories = listOf("Todos") + allItems.map { it.category }.distinct().sorted()
        chipGroup.removeAllViews()
        categories.forEach { cat -> chipGroup.addView(makeChip(cat)) }
    }

    private fun makeChip(label: String): TextView {
        val chip = TextView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.marginEnd = 8
        chip.layoutParams = params
        chip.text = label
        chip.textSize = 13f
        chip.gravity = Gravity.CENTER
        chip.setPadding(24, 12, 24, 12)
        chip.setOnClickListener {
            selectedCategory = label
            currentPage = 1
            applyFilters()
            updateChipStyles()
        }
        return chip
    }

    private fun updateChipStyles() {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as TextView
            val isActive = chip.text == selectedCategory
            chip.setBackgroundColor(if (isActive) 0xFF2F6BFF.toInt() else 0xFFF0F2F5.toInt())
            chip.setTextColor(if (isActive) 0xFFFFFFFF.toInt() else 0xFF7C8492.toInt())
        }
    }

    private fun applyFilters() {
        filteredItems = allItems.filter { item ->
            val matchesCategory = selectedCategory == "Todos" || item.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
        updateChipStyles()
        renderPage()
    }

    private fun renderPage() {
        val totalPages = totalPages()
        val from = (currentPage - 1) * PAGE_SIZE
        val to = minOf(from + PAGE_SIZE, filteredItems.size)
        val pageItems = if (from < filteredItems.size) filteredItems.subList(from, to) else emptyList()

        adapter.submitList(pageItems)
        txtPageInfo.text = "Página $currentPage de ${maxOf(totalPages, 1)}"
        btnPrev.alpha = if (currentPage > 1) 1f else 0.3f
        btnNext.alpha = if (currentPage < totalPages) 1f else 0.3f
    }

    private fun totalPages() = maxOf(1, Math.ceil(filteredItems.size.toDouble() / PAGE_SIZE).toInt())
}
