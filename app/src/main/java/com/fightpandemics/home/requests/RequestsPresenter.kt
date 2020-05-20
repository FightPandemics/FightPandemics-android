package com.fightpandemics.home.requests

class RequestsPresenter(private val view: RequestsContract.View): RequestsContract.Presenter {

    override fun prepareData() {
        view.setContent("REQUESTS")
    }
}