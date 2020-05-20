package com.fightpandemics.home.all

import com.fightpandemics.base.BasePresenter
import com.fightpandemics.base.BaseView

interface AllContract {
    interface Presenter : BasePresenter {
        fun prepareData()
    }

    interface View : BaseView<Presenter> {
        fun setContent(text: String)
    }
}