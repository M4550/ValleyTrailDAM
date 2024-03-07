package com.amm.valleytraildam.model

data class User (
    var bornDate: String? = "",
    var nif: String? = "",
    var password : String? = "",
    var email: String? = "",
    var name : String? = "",
    var phone: String? = "",
    var surname: String? = "",
    var currentRoutes : ArrayList<String>? = arrayListOf(),
    var pastRoutes : ArrayList<String>? = arrayListOf(),
    val isAdmin: Boolean? = false,
    var address: String? = ""
)