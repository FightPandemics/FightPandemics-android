package com.fightpandemics.base

interface BaseView<T : BasePresenter> {
    fun setPresenter(presenter: T)
}