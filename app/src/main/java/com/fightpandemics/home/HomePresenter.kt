package com.fightpandemics.home

import javax.inject.Inject

class HomePresenter @Inject constructor(private val view: HomeContract.View) :
    HomeContract.Presenter
