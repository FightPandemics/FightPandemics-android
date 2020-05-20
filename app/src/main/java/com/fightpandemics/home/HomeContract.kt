package com.fightpandemics.home

import com.fightpandemics.base.BasePresenter
import com.fightpandemics.base.BaseView

interface HomeContract {

    interface Presenter : BasePresenter {

    }

    interface View : BaseView<Presenter> {

    }
}