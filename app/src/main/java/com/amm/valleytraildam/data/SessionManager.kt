package com.amm.valleytraildam.data

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.amm.valleytraildam.R
import com.amm.valleytraildam.ui.view.adminview.AdminHomeActivity
import com.amm.valleytraildam.ui.view.userview.UserHomeActivity
import com.amm.valleytraildam.utils.AdminCheck

class SessionManager {
    companion object {
        fun checkSession(context: Context) {
            val prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val email = prefs.getString("email", null)
            val provider = prefs.getString("provider", null)

            Log.i("SessionManager", "Email: $email")

            if (email != null && provider != null) {
                AdminCheck.isAdmin(email) { isAdmin ->
                    if (isAdmin) {
                        val intent = Intent(context, AdminHomeActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("provider", provider)
                        startActivity(context, intent, null)
                    } else {
                        val intent = Intent(context, UserHomeActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("provider", provider)
                        startActivity(context, intent, null)
                    }
                }
            }



        }
    }
}