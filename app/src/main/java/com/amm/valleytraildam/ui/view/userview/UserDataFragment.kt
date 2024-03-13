package com.amm.valleytraildam.ui.view.userview

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.FragmentUserDataBinding
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.view.mainview.MainActivity
import com.amm.valleytraildam.ui.viewmodel.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDataFragment : Fragment() {

    private lateinit var binding : FragmentUserDataBinding
    private lateinit var viewModel: UserDataViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[UserDataViewModel::class.java]

        Log.i("UserDataFragment", "onViewCreated")

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }


        GlobalScope.launch(Dispatchers.Main) {
            Log.i("UserDataFragment", "GlobalScope.launch")
            val user = viewModel.getUserData()
            Log.i("UserDataFragment", "user = $user")
            if (user != null) {
                binding.userEmail.text = user.email
                binding.userName.text = user.name
                binding.userPhone.text = user.phone
                binding.userAddress.text = user.address
                binding.userSurname.text = user.surname
                binding.userNif.text = user.nif

            }




        }



    }

}