package com.example.naporta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.naporta.network.MenuItemResponse
import java.text.NumberFormat
import java.util.Locale

class MenuGuestAdapter(
    private val onAdd: (MenuItemResponse) -> Unit
) : RecyclerView.Adapter<MenuGuestAdapter.VH>() {

    private var items: List<MenuItemResponse> = emptyList()

    fun submitList(list: List<MenuItemResponse>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_menu_guest, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val img = view.findViewById<ImageView>(R.id.imgDish)
        private val txtName = view.findViewById<TextView>(R.id.txtDishName)
        private val txtDesc = view.findViewById<TextView>(R.id.txtDishDesc)
        private val txtPrice = view.findViewById<TextView>(R.id.txtDishPrice)
        private val btnAdd = view.findViewById<TextView>(R.id.btnAddToCart)

        fun bind(item: MenuItemResponse) {
            txtName.text = item.name
            txtDesc.text = item.description ?: ""
            txtPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(item.price.toDoubleOrNull() ?: 0.0)

            if (!item.image_url.isNullOrBlank()) {
                Glide.with(itemView.context).load(item.image_url).centerCrop().into(img)
            }

            btnAdd.setOnClickListener { onAdd(item) }
            itemView.setOnClickListener { onAdd(item) }
        }
    }
}
