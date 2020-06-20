package com.fightpandemics.home.offers

interface OffersContract {
    interface Presenter {
        fun prepareData()
    }

    interface View {
        fun setContent(text: String)
    }
}
