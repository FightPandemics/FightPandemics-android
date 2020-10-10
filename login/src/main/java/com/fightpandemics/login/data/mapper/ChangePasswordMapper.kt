package com.fightpandemics.login.data.mapper

import com.fightpandemics.login.data.model.ChangePasswordModel
import com.fightpandemics.login.networking.ChangePasswordResponse

class ChangePasswordMapper : BaseMapper<ChangePasswordResponse, ChangePasswordModel> {

    override fun transformRemoteToLocal(input: ChangePasswordResponse): ChangePasswordModel {
        return ChangePasswordModel(input.email, input.responseMessage)
    }
}