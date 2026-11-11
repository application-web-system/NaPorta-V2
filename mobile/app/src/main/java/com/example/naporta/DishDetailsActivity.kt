package com.example.naporta

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DishDetailsActivity : BaseActivity() {
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_details)

        val txtQuantidade = findViewById<TextView>(R.id.txtQuantidade)

        findViewById<ImageView>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<ImageView>(R.id.btnAumentar).setOnClickListener {
            quantity++
            txtQuantidade.text = quantity.toString()
        }

        findViewById<ImageView>(R.id.btnDiminuir).setOnClickListener {
            if (quantity > 1) {
                quantity--
                txtQuantidade.text = quantity.toString()
            }
        }

        findViewById<android.view.View>(R.id.btnAdicionarCarrinho).setOnClickListener {
            CartStore.add(CartItem(
                productId = System.currentTimeMillis(),
                menuItemId = "",
                name = "Item",
                price = 0.0,
                quantity = quantity
            ))
            Toast.makeText(this, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
