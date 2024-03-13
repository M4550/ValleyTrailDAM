package com.amm.valleytraildam.ui.view.adminview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityAdminHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeBinding
    private lateinit var navigation: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)


        navigation = binding.navMenu
        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.fragment1 -> {
                    supportFragmentManager.commit {
                        replace<AdminHomeFragment>(binding.fragmentContainer.id)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }

                R.id.fragment2 -> {
                    supportFragmentManager.commit {
                        replace<AdminHistoryFragment>(binding.fragmentContainer.id)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }


//                R.id.fragment3 -> {
//                    supportFragmentManager.commit {
//                        replace<AdminUsersFragment>(binding.fragmentContainer.id)
//                        setReorderingAllowed(true)
//                        addToBackStack("replacement")
//                    }
//                }



            }

            true
        }

        supportFragmentManager.commit {
            replace<AdminHomeFragment>(binding.fragmentContainer.id)
            setReorderingAllowed(true)
            addToBackStack("replacement")


        }

    }
}