package com.amm.valleytraildam.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amm.valleytraildam.R

class UserHomeFragment : Fragment() {

    companion object {
        fun newInstance() = UserHomeFragment()
    }

    private lateinit var viewModel: UserHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserHomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}