package com.amm.valleytraildam.ui.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.amm.valleytraildam.databinding.ActivityRegisterBinding
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.ui.viewmodel.CheckUserInfo
import com.amm.valleytraildam.ui.ui.viewmodel.DataBaseRegister
import com.amm.valleytraildam.ui.ui.viewmodel.HideKeyboard
import com.amm.valleytraildam.ui.ui.viewmodel.ShowDatePicker
import java.util.Calendar

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var selectedDate: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val calendar: Calendar = Calendar.getInstance()

        binding.datePickerBtn.setOnClickListener {
            ShowDatePicker.showDatePicker(this, binding.root) { formattedDate ->
                selectedDate = formattedDate
            }
        }


        binding.root.setOnClickListener {
            HideKeyboard.hideKeyboard(binding.root)
        }


        val currentUser = User()



        binding.registerBtn.setOnClickListener {
            val name = binding.userName.text.toString()
            val surname = binding.userSurname.text.toString()
            val email = binding.userEmail.text.toString()
            val nif = binding.userNif.text.toString()
            val phone = binding.userPhone.text.toString()
            val password = binding.userPass.text.toString()
            val address = binding.userAddress.text.toString()
            val checkPassword = binding.userCheckPass.text.toString()
            val bornDate = selectedDate

            currentUser.name = name
            currentUser.surname = surname
            currentUser.email = email
            currentUser.phone = phone
            currentUser.address = address
            currentUser.bornDate = bornDate
            currentUser.nif = nif
            currentUser.password = password

            if (CheckUserInfo.checkUserInfo(
                    name, surname, email, phone, password, checkPassword, nif, address, bornDate
                )
            ) {

                if (password == checkPassword) {
                    if (password.length >= 6) {


                        DataBaseRegister.registerUser(currentUser, this)
                    } else {

                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT)
                            .show()
                    }



                    Log.i("Usuario", currentUser.toString())
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this, "Faltan campos por rellenar", Toast.LENGTH_SHORT
                ).show()
            }


        }


    }


}