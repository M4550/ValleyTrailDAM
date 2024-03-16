package com.amm.valleytraildam.ui.view.adminview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.amm.valleytraildam.databinding.RouteItemBinding
import com.amm.valleytraildam.model.Route

class AdminHistoryViewHolder private constructor(
    private val binding: RouteItemBinding,
    private val listener: OnRouteClickListener
) : ViewHolder(binding.root), View.OnClickListener {

    private var route: Route? = null

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(route: Route) {
        this.route = route
        binding.rvTextDate.text = route.date
    }

    override fun onClick(v: View?) {
        route?.let {
            listener.onRouteClick(it)
        }
    }

    interface OnRouteClickListener {
        fun onRouteClick(route: Route)
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnRouteClickListener): AdminHistoryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RouteItemBinding.inflate(inflater, parent, false)
            return AdminHistoryViewHolder(binding, listener)
        }
    }
}