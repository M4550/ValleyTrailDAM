package com.amm.valleytraildam.ui.view.adminview.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.ui.view.adminview.viewholders.AdminHistoryViewHolder

class AdminHistoryAdapter(private val adminHistoryRouteList: List<Route>, private val listener: AdminHistoryViewHolder.OnRouteClickListener) : RecyclerView.Adapter<AdminHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminHistoryViewHolder {
        return AdminHistoryViewHolder.create(parent, listener)
    }

    override fun getItemCount(): Int {
        return adminHistoryRouteList.size
    }

    override fun onBindViewHolder(holder: AdminHistoryViewHolder, position: Int) {
        val item = adminHistoryRouteList[position]
        holder.bind(item)
    }
}