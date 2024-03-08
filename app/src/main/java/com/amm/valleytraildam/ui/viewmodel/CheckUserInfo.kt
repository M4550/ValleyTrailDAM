package com.amm.valleytraildam.ui.viewmodel

class CheckUserInfo {
    companion object {


        fun checkUserInfo(
            name: String,
            surname: String,
            email: String,
            phone: String,
            password: String,
            checkPassword: String,
            nif: String,
            address: String,
            date: String?
        ): Boolean {

            return name.isNotBlank() && surname.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank() && checkPassword.isNotBlank() && nif.isNotBlank() && address.isNotBlank() && date != null
        }


    }

}

