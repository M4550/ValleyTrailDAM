package com.amm.valleytraildam.model

data class Route (
    var date: String? = "",
    var maxParticipants: Int? = 12,
    var participants: Int? = 0,
    var routeName: String? = "",
    var time : String? = "10:00 Horas",
    var users: ArrayList<String>? = arrayListOf(""),
    var isBloqued: Boolean? = false
)
