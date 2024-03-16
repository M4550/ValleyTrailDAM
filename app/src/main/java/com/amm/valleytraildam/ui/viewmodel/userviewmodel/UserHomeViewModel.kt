package com.amm.valleytraildam.ui.viewmodel.userviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amm.valleytraildam.model.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserHomeViewModel : ViewModel() {



    suspend fun getUserRoutes(): List<Route> {
        return withContext(Dispatchers.IO) {
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.email
            var allRoutes: List<Route>
            var userRoutes = mutableListOf<Route>()
            if (userId != null) {
                val routeDocuments = db.collection("active_routes").get().await()
                allRoutes = routeDocuments.toObjects(Route::class.java)

                for (route in allRoutes){
                    if (route.users?.contains(userId) == true) {
                        userRoutes.add(route)
                        Log.i("UserDataViewModel", "userRoute = $userRoutes")
                    }
                }


            }
            userRoutes
        }
    }









}