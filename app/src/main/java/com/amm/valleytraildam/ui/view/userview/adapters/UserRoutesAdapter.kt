package com.amm.valleytraildam.ui.view.userview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amm.valleytraildam.R
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.ui.view.userview.viewholders.UserRoutesViewHolder

class UserRoutesAdapter(private val userRoutesList : List<Route>): RecyclerView.Adapter<UserRoutesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRoutesViewHolder {
         val layoutInflater = LayoutInflater.from(parent.context)
        return UserRoutesViewHolder(layoutInflater.inflate(R.layout.route_item, parent, false))
    }

    override fun getItemCount(): Int {
        return userRoutesList.size
    }

    override fun onBindViewHolder(holder: UserRoutesViewHolder, position: Int) {


        val item = userRoutesList[position]
        holder.render(item)
    }
}