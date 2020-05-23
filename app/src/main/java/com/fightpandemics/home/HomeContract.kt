package com.fightpandemics.home

import com.fightpandemics.base.BasePresenter
import com.fightpandemics.base.BaseView
import dagger.Component
import dagger.Provides

interface HomeContract {

    interface Presenter : BasePresenter {

    }

    interface View : BaseView<Presenter> {

    }
}