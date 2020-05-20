package com.fightpandemics.home.offers

class OffersPresenter(private val view: OffersContract.View) : OffersContract.Presenter {

    override fun prepareData() {
        view.setContent("OFFERS")
    }
}