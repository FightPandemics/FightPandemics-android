package com.fightpandemics.home.offers

import javax.inject.Inject

class OffersPresenter @Inject constructor(private val view: OffersContract.View) : OffersContract.Presenter {

    override fun prepareData() {
        view.setContent("OFFERS")
    }
}