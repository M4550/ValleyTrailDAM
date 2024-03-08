package com.amm.valleytraildam.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityAvailableRoutesBinding

class AvailableRoutesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableRoutesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableRoutesBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.ancestralesCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "anc")
            startActivity(intent)
        }
        binding.congostoCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "cong")
            startActivity(intent)

        }
        binding.civilCard.setOnClickListener {
            val intent = Intent(this, HomeRouteActivity::class.java)
            intent.putExtra("routeKey", "civ")
            startActivity(intent)

        }
    }
}