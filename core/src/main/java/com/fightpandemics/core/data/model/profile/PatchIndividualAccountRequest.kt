package com.fightpandemics.core.data.model.profile

data class PatchIndividualAccountRequest(
    val firstName: String,
    val hide: Hide,
    val lastName: String,
    val location: Location,
    val needs: Needs,
    val objectives: Objectives
)

