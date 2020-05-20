package com.fightpandemics.home.requests

import com.fightpandemics.base.BasePresenter
import com.fightpandemics.base.BaseView

interface RequestsContract {
    interface Presenter : BasePresenter {

    }

    interface View : BaseView<Presenter> {

    }
}