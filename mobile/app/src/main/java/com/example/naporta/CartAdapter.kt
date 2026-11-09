package com.example.naporta
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

/*
    # CartAdapter
    - Ponte entre os dados e o XML
    - Pega o CartItem e preenche o item_cart.xml (nome, preço, imagem, botões)
 */

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var items: List<CartItem> = emptyList()

    fun submitList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        private val txtName = view.findViewById<android.widget.TextView>(R.id.txtName)
        private val txtPrice = view.findViewById<android.widget.TextView>(R.id.txtPrice)
        private val txtQuantity = view.findViewById<android.widget.TextView>(R.id.txtQuantity)
        private val imgProduct = view.findViewById<android.widget.ImageView>(R.id.imgProduct)
        private val btnIncrease = view.findViewById<android.view.View>(R.id.btnIncrease)
        private val btnDecrease = view.findViewById<android.view.View>(R.id.btnDecrease)
        private val btnDelete = view.findViewById<android.view.View>(R.id.btnDelete)

        fun bind(item: CartItem) {
            txtName.text = item.name
            txtPrice.text = money(item.unitPrice)
            txtQuantity.text = item.quantity.toString()
            item.imageRes?.let(imgProduct::setImageResource) ?: imgProduct.setImageResource(R.drawable.img_hamburguer)
            btnIncrease.setOnClickListener { onIncrease(item) }
            btnDecrease.setOnClickListener { onDecrease(item) }
            btnDelete.setOnClickListener { onRemove(item) }
        }
    }

    companion object {
        fun money(value: Double): String = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }
}
