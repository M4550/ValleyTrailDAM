package com.amm.valleytraildam.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityHomeRouteBinding

class HomeRouteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeRouteBinding
    private lateinit var routeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeRouteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setRouteContent(intent)

        binding.ibAddRoute.setOnClickListener {
            intent.extras?.let {
                routeId = it.getString("routeKey").toString()
            }

            val intent = Intent(this, RequestRouteActivity::class.java)
            intent.putExtra("routeKey", routeId)
            startActivity(intent)
        }


    }


    private fun setRouteContent(intent: Intent?) {
        if (intent != null) {
            intent.extras?.let {
                val message = it.getString("routeKey")
                when (message) {
                    "anc" -> {
                        binding.imMain.setImageResource(R.drawable.im_ancestrales_main)
                        binding.im1.setImageResource(R.drawable.im_anc1)
                        binding.im2.setImageResource(R.drawable.im_anc2)
                        binding.im3.setImageResource(R.drawable.im_anc3)
                        binding.im4.setImageResource(R.drawable.im_anc4)
                        binding.title.setText(R.string.caminos_ancestrales)
                        binding.tvDesc.setText(R.string.anc_description)
                        binding.cvCardText.setText(R.string.anc_user_info_card)


                    }

                    "cong" -> {
                        binding.imMain.setImageResource(R.drawable.im_cong_main)
                        binding.im1.setImageResource(R.drawable.im_cong1)
                        binding.im2.setImageResource(R.drawable.im_cong2)
                        binding.im3.setImageResource(R.drawable.im_cong3)
                        binding.im4.setImageResource(R.drawable.im_cong4)
                        binding.title.setText(R.string.a_traves_del_congosto)
                        binding.tvDesc.setText(R.string.cong_description)
                        binding.cvCardText.setText(R.string.cong_user_info_card)
                    }

                    "civ" -> {
                        binding.imMain.setImageResource(R.drawable.im_civ_main)
                        binding.im1.setImageResource(R.drawable.im_civ1)
                        binding.im2.setImageResource(R.drawable.im_civ2)
                        binding.im3.setImageResource(R.drawable.im_civ3)
                        binding.im4.setImageResource(R.drawable.im_civ4)
                        binding.title.setText(R.string.civilizaciones_pasadas)
                        binding.tvDesc.setText(R.string.civ_description)
                        binding.cvCardText.setText(R.string.civ_user_info_card)
                    }

                }
            }
        }

    }
}