package com.amm.valleytraildam.ui.view.mainview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amm.valleytraildam.databinding.ActivityLoginBinding
import com.amm.valleytraildam.data.Login

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.etEmail.requestFocus()



        binding.loginBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            Login.loginUser(email, password, this)
        }

    }
}