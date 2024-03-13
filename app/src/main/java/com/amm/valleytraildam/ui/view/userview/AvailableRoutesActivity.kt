package com.amm.valleytraildam.ui.view.userview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amm.valleytraildam.databinding.ActivityAvailableRoutesBinding

class AvailableRoutesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableRoutesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableRoutesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val isLogged = intent.extras?.getBoolean("isLogged")

        Log.i(" isLogged", "$isLogged")





        binding.ancestralesCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "anc")
            intent.putExtra("isLogged", isLogged)
            startActivity(intent)
        }
        binding.congostoCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "cong")
            intent.putExtra("isLogged", isLogged)
            startActivity(intent)

        }
        binding.civilCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "civ")
            intent.putExtra("isLogged", isLogged)
            startActivity(intent)

        }
    }
}