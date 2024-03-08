package com.amm.valleytraildam.ui.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.amm.valleytraildam.R
import com.amm.valleytraildam.model.model.User
import com.amm.valleytraildam.ui.viewmodel.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDataFragment : Fragment() {

    private var user: User = User()
    private lateinit var userEmailEditText: TextView
    private lateinit var userNameEditText: TextView
    private lateinit var userPhoneEditText: TextView
    private lateinit var userAddressEditText: TextView
    private lateinit var userSurnameEditText: TextView
    private lateinit var userNifEditText: TextView
    private lateinit var logoutButton: com.google.android.material.button.MaterialButton

    private lateinit var viewModel: UserDataViewModel

    companion object {
        fun newInstance() = UserDataFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutButton = view.findViewById(R.id.logoutBtn)
        userEmailEditText  = view.findViewById(R.id.userEmail)
        userNameEditText = view.findViewById(R.id.userName)
        userPhoneEditText = view.findViewById(R.id.userPhone)
        userAddressEditText = view.findViewById(R.id.userAddress)
        userSurnameEditText = view.findViewById(R.id.userSurname)
        userNifEditText = view.findViewById(R.id.userNif)
        viewModel = ViewModelProvider(this)[UserDataViewModel::class.java]

        Log.i("UserDataFragment", "onViewCreated")

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }


        GlobalScope.launch(Dispatchers.Main) {
            Log.i("UserDataFragment", "GlobalScope.launch")
            val user = viewModel.getUserData()
            Log.i("UserDataFragment", "user = $user")
            if (user != null) {
                userEmailEditText.text = user.email
                userNameEditText.text = user.name
                userPhoneEditText.text = user.phone
                userAddressEditText.text = user.address
                userSurnameEditText.text = user.surname
                userNifEditText.text = user.nif

            }




        }



    }

}