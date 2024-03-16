package com.amm.valleytraildam.ui.view.userview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityUserHomeBinding

import com.amm.valleytraildam.databinding.AppToolbarBinding
import com.amm.valleytraildam.ui.viewmodel.userviewmodel.UserDataViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class UserHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var iconToolbar: ImageButton
    private lateinit var bindingToolbar: AppToolbarBinding
    val fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingToolbar = AppToolbarBinding.inflate(layoutInflater)





        val bundle = intent?.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        // Guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.putString("provider", provider)
        editor.apply()

        MainScope().launch {
            if (UserDataViewModel.getUserData()?.nif.isNullOrBlank() ||
                UserDataViewModel.getUserData()?.address.isNullOrBlank() ||
                UserDataViewModel.getUserData()?.phone.isNullOrBlank() ||
                UserDataViewModel.getUserData()?.name.isNullOrBlank() ||
                UserDataViewModel.getUserData()?.surname.isNullOrBlank()) {

                val builder = AlertDialog.Builder(this@UserHomeActivity)
                builder.setTitle(getString(R.string.datos_personales_requeridos))
                builder.setMessage(getString(R.string.por_favor_complete_sus_datos_personales_para_poder_contratar_una_ruta))

                builder.setPositiveButton(getString(R.string.completar)) { dialog, _ ->
                    dialog.dismiss()
                    val userDataFragment = UserDataFragment()

                    // Reemplaza el fragmento actual con UserDataFragment
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, userDataFragment).commit()
                }

                builder.setNegativeButton(getString(R.string.m_s_tarde)) { dialog, _ ->


                    dialog.dismiss()
                }
                builder.show()
            }


        }












        drawer = binding.drawerLayout
        navigationView = binding.navView
        iconToolbar = binding.imIconToolbar
        loadFragment(fragment = UserHomeFragment())

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
                    loadFragment(fragment = UserHomeFragment())

                    drawer.closeDrawer(navigationView)


                }

                R.id.nav_item2 -> {
                    val intent = Intent(this, AvailableRoutesActivity::class.java)
                    intent.putExtra("isLogged", true)
                    startActivity(intent)


                }

                R.id.nav_item3 -> {
                    loadFragment(fragment = UserDataFragment())
                    drawer.closeDrawer(navigationView)

                }

                R.id.nav_item4 -> {

                    // Dirección de correo electrónico a la que se enviará el correo electrónico
                    val emailAddress = "luciamalo98@gmail.com"

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
                        Toast.makeText(
                            this,
                            "No se encontró ninguna aplicación de correo electrónico",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                R.id.nav_item5 -> {
                    val phoneNumber = 676864397

                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phoneNumber")

                    Log.i("TAG", "Intent: ${intent.resolveActivity(packageManager)}")

                    if (intent.resolveActivity(packageManager) != null) {
                        Toast.makeText(this, "LLamando", Toast.LENGTH_SHORT).show()
                        // Iniciar la actividad de llamada
                        startActivity(intent)
                    }
                    Toast.makeText(this, "No se pudo llamar", Toast.LENGTH_SHORT).show()

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
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

}