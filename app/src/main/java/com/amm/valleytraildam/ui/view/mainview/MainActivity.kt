package com.amm.valleytraildam.ui.view.mainview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amm.valleytraildam.databinding.ActivityMainBinding
import com.amm.valleytraildam.ui.view.userview.AvailableRoutesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.enterBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.enterNoLoginBtn.setOnClickListener {
            val intent = Intent(this, AvailableRoutesActivity::class.java)
            intent.putExtra("noLogin", true)
            startActivity( intent)
        }



    }
}