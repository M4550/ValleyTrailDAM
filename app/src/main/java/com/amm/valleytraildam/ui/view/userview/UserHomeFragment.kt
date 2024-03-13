package com.amm.valleytraildam.ui.view.userview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.amm.valleytraildam.databinding.FragmentUserHomeBinding
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.ui.viewmodel.userviewmodel.UserHomeViewModel
import com.amm.valleytraildam.ui.viewmodel.userviewmodel.UserRoutesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class UserHomeFragment : Fragment() {
    private lateinit var binding : FragmentUserHomeBinding
    private lateinit var viewModel: UserHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var routeList: List<Route>
        viewModel = ViewModelProvider(this)[UserHomeViewModel::class.java]


        GlobalScope.launch(Dispatchers.Main) {
            routeList = viewModel.getUserRoutes()

            fun initrecyclerView(){
                val recyclerView = binding.rvRoutes
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = UserRoutesAdapter(routeList)
            }
            initrecyclerView()
        }








    }



}