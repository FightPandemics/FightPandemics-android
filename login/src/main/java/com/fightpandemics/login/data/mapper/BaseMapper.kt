package com.fightpandemics.login.data.mapper

interface BaseMapper<I, O> {
    fun transformRemoteToLocal(input: I): O
}