package com.amm.valleytraildam.ui.view.mainview

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityMainBinding
import com.amm.valleytraildam.ui.view.userview.AvailableRoutesActivity
import com.amm.valleytraildam.data.Login
import com.amm.valleytraildam.data.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        SessionManager.checkSession(this)

        val signInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task)
                }
            }

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
            startActivity(intent)
        }


        binding.googleBtn.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this@MainActivity, googleConf)

            val signInIntent = googleClient.signInIntent
            signInLauncher.launch(signInIntent)
        }


    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val email = account?.email
            val name = account?.displayName
            val idToken = account?.idToken

            // Iniciar sesión con Firebase Auth utilizando el token de ID de Google
            Login.loginUserWithGoogle(idToken, this)

            // Guardar los datos del usuario en Firestore
            val db = FirebaseFirestore.getInstance()


            email?.let { email ->
                val userMap = mapOf(
                    "name" to name,
                    "isAdmin" to false
                    // Puedes agregar otros campos aquí
                )

                db.collection("users").document(email).update(userMap)
                    .addOnSuccessListener {
                        Log.d(TAG, "Campos de usuario actualizados correctamente en Firestore")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al actualizar campos de usuario en Firestore", e)
                    }
            }

        } catch (e: ApiException) {
            // Error al iniciar sesión con Google
            Log.e(TAG, "Error en el proceso de inicio de sesión con Google: =" + e.statusCode)
            // Aquí puedes manejar el error, como mostrar un mensaje al usuario o intentar iniciar sesión de otra manera
        }
    }


}