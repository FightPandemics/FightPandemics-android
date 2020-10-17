package com.fightpandemics.login.data.mapper

import com.fightpandemics.login.data.model.ChangePasswordModel
import com.fightpandemics.login.networking.ChangePasswordResponse

class ChangePasswordMapper : BaseMapper<ChangePasswordResponse, ChangePasswordModel> {

    override fun transformRemoteToLocal(input: ChangePasswordResponse): ChangePasswordModel {
        val model = ChangePasswordModel()
        model.email = input.email
        model.responseMessage = input.responseMessage
        model.error = input.error
        model.message = input.message
        model.statusCode = input.statusCode
        return model
        //return ChangePasswordModel(input.email, input.responseMessage)
    }
}