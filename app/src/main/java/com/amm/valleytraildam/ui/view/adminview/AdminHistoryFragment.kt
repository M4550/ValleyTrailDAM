package com.amm.valleytraildam.ui.view.adminview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amm.valleytraildam.databinding.FragmentAdminHistoryBinding
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminHistoryAdapter
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminHistoryViewHolder
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminHistoryFragment : Fragment(), AdminHistoryViewHolder.OnRouteClickListener  {

    private lateinit var binding: FragmentAdminHistoryBinding
    private lateinit var viewModel: AdminHomeViewModel
    private lateinit var adapter: AdminHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminHomeViewModel::class.java]

        GlobalScope.launch(Dispatchers.Main) {
            val adminHistoryRouteList = viewModel.getHistoryRoutes()
            initRecyclerView(adminHistoryRouteList)
        }
    }

    private fun initRecyclerView(adminHistoryRouteList: List<Route>) {
        adapter = AdminHistoryAdapter(adminHistoryRouteList, this)
        binding.rvRoutes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutes.adapter = adapter
    }

    override fun onRouteClick(route: Route) {
        route.date?.let { Log.i("route", it) }
        val intent = Intent(activity, AdminRouteDetailsActivity::class.java)
        intent.putExtra("date", route.date)
        intent.putExtra("isActive", route.isActive)

        startActivity(intent)
    }
}