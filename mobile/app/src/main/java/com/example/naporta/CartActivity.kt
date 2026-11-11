package com.example.naporta

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naporta.network.CreateOrderRequest
import com.example.naporta.network.OrderItemRequest
import com.example.naporta.network.RetrofitClient
import kotlinx.coroutines.launch

class CartActivity : BaseActivity() {
    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtSubtotal: TextView
    private lateinit var txtTotal: TextView
    private lateinit var btnCheckout: TextView
    private val adapter = CartAdapter(
        onIncrease = { CartStore.increase(it.productId) },
        onDecrease = { CartStore.decrease(it.productId) },
        onRemove = { CartStore.remove(it.productId) }
    )
    private val cartObserver: (List<CartItem>) -> Unit = { items -> render(items) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerCart = findViewById(R.id.recyclerCart)
        txtSubtotal = findViewById(R.id.txtSubtotal)
        txtTotal = findViewById(R.id.txtTotal)
        btnCheckout = findViewById(R.id.btnCheckout)

        recyclerCart.layoutManager = LinearLayoutManager(this)
        recyclerCart.adapter = adapter

        findViewById<android.view.View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.btnContinueShopping).setOnClickListener { finish() }

        btnCheckout.setOnClickListener { submitOrder() }
    }

    override fun onStart() {
        super.onStart()
        CartStore.observe(cartObserver)
    }

    override fun onStop() {
        CartStore.removeObserver(cartObserver)
        super.onStop()
    }

    private fun render(items: List<CartItem>) {
        adapter.submitList(items)
        val subtotal = items.sumOf(CartItem::lineTotal)
        txtSubtotal.text = CartAdapter.money(subtotal)
        txtTotal.text = CartAdapter.money(subtotal)
        btnCheckout.isEnabled = items.isNotEmpty()
        btnCheckout.alpha = if (items.isNotEmpty()) 1f else .5f
    }

    private fun submitOrder() {
        val guestId = SessionManager.getGuestId(this)
        if (guestId == null) {
            Toast.makeText(this, "Identifique-se primeiro. Escaneie o QRCode do seu quarto.", Toast.LENGTH_LONG).show()
            startActivity(android.content.Intent(this, ScanTokenActivity::class.java))
            return
        }
        val items = CartStore.items
        if (items.isEmpty()) {
            Toast.makeText(this, "Seu carrinho está vazio.", Toast.LENGTH_SHORT).show()
            return
        }

        val orderItems = items.map { OrderItemRequest(it.menuItemId, it.quantity) }

        btnCheckout.isEnabled = false
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createOrder(
                    CreateOrderRequest(guestId, orderItems)
                )
                if (response.isSuccessful || response.code() == 200) {
                    CartStore.clear()
                    Toast.makeText(this@CartActivity, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@CartActivity, "Erro ao enviar pedido.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Sem conexão.", Toast.LENGTH_SHORT).show()
            } finally {
                btnCheckout.isEnabled = true
            }
        }
    }
}
