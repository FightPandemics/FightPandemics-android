package com.fightpandemics.home.offers

import com.fightpandemics.base.BasePresenter
import com.fightpandemics.base.BaseView

interface OffersContract {
    interface Presenter : BasePresenter {
        fun prepareData()
    }

    interface View : BaseView<Presenter> {
        fun setContent(text: String)
    }
}