package com.amm.valleytraildam.ui.view.userview

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.FragmentUserDataBinding
import com.amm.valleytraildam.ui.view.mainview.MainActivity
import com.amm.valleytraildam.ui.viewmodel.userviewmodel.UserDataViewModel
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
    ): View {
        binding = FragmentUserDataBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[UserDataViewModel::class.java]

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }

        binding.editProfileBtn.setOnClickListener {
            enableFieldsForEditing()
            binding.editProfileBtn.text = getString(R.string.guardar)
            binding.editProfileBtn.setOnClickListener {
                saveChangesToFirebase()
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            Log.i("UserDataFragment", "GlobalScope.launch")
            val user = UserDataViewModel.getUserData()
            Log.i("UserDataFragment", "user = $user")
            if (user != null) {
                binding.userEmail.setText(user.email)
                binding.userName.setText(user.name)
                binding.userSurname.setText(user.surname)
                binding.userPhone.setText(user.phone)
                binding.userAddress.setText(user.address)
                binding.userNif.setText(user.nif)

            }




        }



    }

    private fun enableFieldsForEditing() {

        // Habilita la edición de los campos de texto

        binding.userAddress.isEnabled = true
        binding.userAddress.hint = getString(R.string.registerAddress)
        binding.userNif.isEnabled = true
        binding.userNif.hint = getString(R.string.registerNif)
        binding.userPhone.isEnabled = true
        binding.userPhone.hint = getString(R.string.registerPhone)
        binding.userName.isEnabled = true
        binding.userName.hint = getString(R.string.registerName)
        binding.userSurname.isEnabled = true
        binding.userSurname.hint = getString(R.string.registerSurname)
    }

    private fun saveChangesToFirebase() {

        val newAddress = binding.userAddress.text.toString()
        val newNif = binding.userNif.text.toString()
        val newPhone = binding.userPhone.text.toString()
        val newName = binding.userName.text.toString()
        val newSurname = binding.userSurname.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            UserDataViewModel.saveUserData(newAddress, newNif, newPhone, newName, newSurname)
            disableFieldsForEditing()
            binding.editProfileBtn.text = getString(R.string.editar_perfil)
        }
    }

    private fun disableFieldsForEditing() {
        // Deshabilita la edición de los campos de texto
        binding.userAddress.isEnabled = false
        binding.userAddress.hint = null

        binding.userNif.isEnabled = false
        binding.userNif.hint = null

        binding.userPhone.isEnabled = false
        binding.userPhone.hint = null

        binding.userName.isEnabled = false
        binding.userName.hint = null

        binding.userSurname.isEnabled = false
        binding.userSurname.hint = null
    }

}