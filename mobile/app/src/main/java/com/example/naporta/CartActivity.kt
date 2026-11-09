package com.example.naporta

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

/*
    # CartActivity
    - carrega o activity_cart.xml
    - usa o adapter
    - observa o store
    - calcula o total
 */

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerCart: androidx.recyclerview.widget.RecyclerView
    private lateinit var txtSubtotal: android.widget.TextView
    private lateinit var txtTotal: android.widget.TextView
    private lateinit var btnCheckout: android.widget.TextView
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
        btnCheckout.setOnClickListener {
            if (CartStore.items.isEmpty()) Toast.makeText(this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Pedido pronto para enviar", Toast.LENGTH_SHORT).show()
        }
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
}
