package com.amm.valleytraildam.ui.ui.view

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.amm.valleytraildam.databinding.ActivityUserHomeBinding

class UserHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}