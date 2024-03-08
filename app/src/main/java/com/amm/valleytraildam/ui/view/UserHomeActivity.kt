package com.amm.valleytraildam.ui.view

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityUserHomeBinding

import com.amm.valleytraildam.databinding.AppToolbarBinding
import com.google.android.material.navigation.NavigationView

class UserHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var iconToolbar: ImageButton
    private lateinit var bindingToolbar: AppToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingToolbar = AppToolbarBinding.inflate(layoutInflater)

        drawer = binding.drawerLayout
        navigationView = binding.navView
        iconToolbar = binding.imIconToolbar

        iconToolbar.setOnClickListener {
            Toast.makeText(this, "Icono clickeado", Toast.LENGTH_SHORT).show()
            if (drawer.isDrawerOpen(navigationView)) {
                drawer.closeDrawer(navigationView)
            } else {
                drawer.openDrawer(navigationView)
            }
        }

        val toggle = ActionBarDrawerToggle(

            this,
            drawer,
            bindingToolbar.toolbarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar acciones cuando el DrawerLayout se abre o se cierra
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })


        navigationView.setNavigationItemSelectedListener { menuItem ->

            // Manejar clics en los elementos del menú
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    drawer.closeDrawer(navigationView)



                }

                R.id.nav_item2 -> {
                    val intent = Intent(this, AvailableRoutesActivity::class.java)
                    startActivity(intent)




                }

                R.id.nav_item3 -> {
                    loadFragment(fragment = UserDataFragment())
                    drawer.closeDrawer(navigationView)

                }
                R.id.nav_item4 -> {

                        // Dirección de correo electrónico a la que se enviará el correo electrónico
                        val emailAddress = "lucia@eseraycinca.com"

                        // Crea un intent para abrir la aplicación de correo electrónico predeterminada
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            // Especifica el destinatario
                            data = Uri.parse("mailto:$emailAddress")
                        }

                        // Verifica si hay una aplicación de correo electrónico disponible para manejar el intent
                        if (emailIntent.resolveActivity(packageManager) != null) {
                            // Abre la aplicación de correo electrónico predeterminada
                            startActivity(emailIntent)
                        } else {
                            // Si no hay ninguna aplicación de correo electrónico disponible, muestra un mensaje al usuario
                            Toast.makeText(this, "No se encontró ninguna aplicación de correo electrónico", Toast.LENGTH_SHORT).show()
                        }



                }
            }
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView)
                } else {
                    drawer.openDrawer(navigationView)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

}