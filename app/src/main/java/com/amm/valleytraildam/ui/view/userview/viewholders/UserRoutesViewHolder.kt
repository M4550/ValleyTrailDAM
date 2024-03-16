package com.amm.valleytraildam.ui.view.userview.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.amm.valleytraildam.R
import com.amm.valleytraildam.model.Route

class UserRoutesViewHolder(itemView: View) : ViewHolder(itemView) {


    private val routeDate: TextView = itemView.findViewById(R.id.rvTextDate)
    private val routeTime: TextView = itemView.findViewById(R.id.rvTextTime)
    private val routeTitle: TextView = itemView.findViewById(R.id.rvTextTitle)
    fun render(route: Route) {
        routeDate.text = route.date
        routeTime.text = route.time
        routeTitle.text = route.routeName
    }
}