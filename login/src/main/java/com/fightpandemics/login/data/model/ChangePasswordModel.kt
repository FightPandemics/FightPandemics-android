package com.fightpandemics.login.data.model


class ChangePasswordModel : DomainErrorModel(){
    var email: String? = null
    var responseMessage: String? = null
}