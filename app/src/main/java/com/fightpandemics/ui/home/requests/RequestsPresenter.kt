package com.fightpandemics.ui.home.requests

import javax.inject.Inject

class RequestsPresenter @Inject constructor(private val view: RequestsContract.View) :
    RequestsContract.Presenter {

    override fun prepareData() {
        view.setContent("REQUESTS")
    }
}