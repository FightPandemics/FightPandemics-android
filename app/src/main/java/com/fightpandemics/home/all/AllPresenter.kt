package com.fightpandemics.home.all

import javax.inject.Inject

class AllPresenter @Inject constructor(private val view: AllContract.View) : AllContract.Presenter {

    override fun prepareData() {
        view.setContent("ALL")
    }
}
