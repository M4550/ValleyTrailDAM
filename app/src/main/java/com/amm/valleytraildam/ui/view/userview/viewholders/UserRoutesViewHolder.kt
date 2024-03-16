package com.amm.valleytraildam.ui.view.userview.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.amm.valleytraildam.R
import com.amm.valleytraildam.model.Route

class UserRoutesViewHolder(itemView: View) : ViewHolder(itemView) {


    val routeDate: TextView = itemView.findViewById(R.id.rvTextDate)

    fun render(route: Route) {
        routeDate.text = route.date

    }
}