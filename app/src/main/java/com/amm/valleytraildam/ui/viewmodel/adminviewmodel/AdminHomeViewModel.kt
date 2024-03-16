package com.amm.valleytraildam.ui.viewmodel.adminviewmodel

import androidx.lifecycle.ViewModel
import com.amm.valleytraildam.model.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdminHomeViewModel: ViewModel() {
    suspend fun getHistoryRoutes(): List<Route> {
        return withContext(Dispatchers.IO) {
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.email
            val allHistoryRoutes: List<Route>
            val adminHistoryRoutes = mutableListOf<Route>()
            if (userId != null) {
                val routeDocuments = db.collection("archived_routes").get().await()
                allHistoryRoutes = routeDocuments.toObjects(Route::class.java)

                for (route in allHistoryRoutes){

                        adminHistoryRoutes.add(route)

                }


            }
            adminHistoryRoutes
        }
    }
}