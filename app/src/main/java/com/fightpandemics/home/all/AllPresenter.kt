package com.fightpandemics.home.all

class AllPresenter(private val view: AllContract.View) : AllContract.Presenter {

    override fun prepareData() {
        view.setContent("ALL")
    }
}