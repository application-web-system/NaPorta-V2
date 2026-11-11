package com.example.naporta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.naporta.network.UserResponse

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.VH>() {

    private var items: List<UserResponse> = emptyList()

    fun submitList(list: List<UserResponse>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val txtName = view.findViewById<TextView>(R.id.txtItemUserName)
        private val txtEmail = view.findViewById<TextView>(R.id.txtItemUserEmail)
        private val txtRole = view.findViewById<TextView>(R.id.txtItemUserRole)

        fun bind(user: UserResponse) {
            txtName.text = user.name
            txtEmail.text = user.email
            txtRole.text = user.role.replaceFirstChar { it.uppercase() }
        }
    }
}
