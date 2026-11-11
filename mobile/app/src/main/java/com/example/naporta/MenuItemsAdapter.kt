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

class MenuItemsAdapter : RecyclerView.Adapter<MenuItemsAdapter.VH>() {

    private var items: List<MenuItemResponse> = emptyList()

    fun submitList(list: List<MenuItemResponse>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_menu_admin, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val img = view.findViewById<ImageView>(R.id.imgMenuItem)
        private val txtName = view.findViewById<TextView>(R.id.txtMenuItemName)
        private val txtCategory = view.findViewById<TextView>(R.id.txtMenuItemCategory)
        private val txtPrice = view.findViewById<TextView>(R.id.txtMenuItemPrice)
        private val txtStatus = view.findViewById<TextView>(R.id.txtMenuItemStatus)

        fun bind(item: MenuItemResponse) {
            txtName.text = item.name
            txtCategory.text = item.category
            txtPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(item.price.toDoubleOrNull() ?: 0.0)

            if (item.available) {
                txtStatus.text = "Disponível"
                txtStatus.setTextColor(android.graphics.Color.parseColor("#00A859"))
                txtStatus.setBackgroundResource(R.drawable.bg_status_delivered)
            } else {
                txtStatus.text = "Indisponível"
                txtStatus.setTextColor(android.graphics.Color.parseColor("#7C8492"))
                txtStatus.setBackgroundResource(R.drawable.bg_chip_inactive)
            }

            if (!item.image_url.isNullOrBlank()) {
                Glide.with(itemView.context).load(item.image_url).centerCrop().into(img)
            }
        }
    }
}
