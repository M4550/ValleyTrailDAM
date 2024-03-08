package com.amm.valleytraildam.model.model

data class Route (
    var date: String? = "",
    var maxParticipants: Int? = 12,
    var participants: Int? = 0,
    var routeName: String? = "",
    var time : String? = "",
    var users: ArrayList<String>? = arrayListOf(""),
    var isActive: Boolean? = false
)
