package com.amm.valleytraildam.ui.view.adminview.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.view.adminview.viewholders.AdminUsersViewHolder

class AdminUsersAdapter(private val adminUserList: List<User>, private val listener: AdminUsersViewHolder.OnUserClickListener) : RecyclerView.Adapter<AdminUsersViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsersViewHolder {
        return AdminUsersViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: AdminUsersViewHolder, position: Int) {
        val item = adminUserList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return adminUserList.size
    }
}